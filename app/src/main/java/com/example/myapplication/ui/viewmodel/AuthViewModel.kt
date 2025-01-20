package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import com.example.myapplication.ui.viewmodel.state.AuthAction
import com.example.myapplication.ui.viewmodel.state.AuthStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import java.security.MessageDigest


class AuthViewModel(private val userRepository: UserRepository,
                    private val application: Application) : ViewModel() {

    private val _authStatus = MutableLiveData<AuthStatus>()
    val authStatus: LiveData<AuthStatus> get() = _authStatus
    private var generatedCode: String? = null

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(application, gso)
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun handleGoogleSignInResult(data: Intent?, context: Context) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken ?: throw ApiException(Status.RESULT_INTERNAL_ERROR)
            val email = account.email ?: throw ApiException(Status.RESULT_INTERNAL_ERROR)

            UserSessionManager.saveUserId(context,idToken)
            val userDTO = UserDTO(userId = idToken, email = email)

            viewModelScope.launch {
                userRepository.registerUser(userDTO)
                _authStatus.value = AuthStatus.Success(AuthAction.LOGIN)
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google 로그인 실패: ${e.message}, StatusCode: ${e.statusCode}", e)
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN,"Google 로그인 실패: ${e.message}")
        }
    }
    suspend fun login(username: String, password: String, context: Context): Boolean {
        _authStatus.value = AuthStatus.Loading(AuthAction.LOGIN)

        if (username.isEmpty() || password.isEmpty()) {
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "아이디와 비밀번호를 다시 입력해주세요")
            Log.w("Login", "아이디 또는 비밀번호가 비어 있습니다.")
            return false
        }

        return try {
            val user = userRepository.getUserByUsername(username)
            if (user != null && BCrypt.checkpw(password, user.password)) {
                UserSessionManager.saveUserId(context, user.userId)
                _authStatus.value = AuthStatus.Success(AuthAction.LOGIN)
                Log.i("Login", "로그인 성공: 사용자 ID = ${user.userId}")
                true
            } else {
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "아이디 또는 비밀번호가 잘못되었습니다.")
                Log.w("Login", "로그인 실패: 사용자 정보가 없거나 비밀번호가 잘못되었습니다.")
                false
            }
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "오류가 발생했습니다.")
            Log.e("Login", "로그인 중 예외 발생: ${e.message}", e)
            false
        }
    }




    fun logout(context: Context) {
        _authStatus.value = AuthStatus.Loading(AuthAction.LOGOUT)
        try {
            UserSessionManager.clearUserSession(context)
            _authStatus.value = AuthStatus.Success(AuthAction.LOGOUT)
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGOUT, "로그아웃 실패")
        }
    }
    suspend fun registerUser(userDTO: UserDTO) = withContext(Dispatchers.IO) {
        try {
            // 비밀번호 해싱
            val hashedPassword = hashPassword(userDTO.password)
            val hashedUserDTO = userDTO.copy(password = hashedPassword)

            // 유효성 검사
            if (!isValidUser(hashedUserDTO)) {
                Log.w("AuthViewModel", "Invalid user data: $hashedUserDTO")
                _authStatus.postValue(
                    AuthStatus.Failure(AuthAction.REGISTER, "유효하지 않은 사용자 정보입니다.")
                )
                return@withContext // 유효성 검사 실패 시 함수 중단
            }

            // 회원가입 처리 시작
            _authStatus.postValue(AuthStatus.Loading(AuthAction.REGISTER))

            try {
                userRepository.registerUser(hashedUserDTO) // 사용자 등록
                Log.d("AuthViewModel", "User registration successful")
                _authStatus.postValue(AuthStatus.Success(AuthAction.REGISTER)) // 성공 상태 설정
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error during user registration: ${e.message}", e)
                _authStatus.postValue(
                    AuthStatus.Failure(AuthAction.REGISTER, "회원가입 중 오류가 발생했습니다.")
                )
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Unhandled exception in registerUser: ${e.message}", e)
        }
    }



    suspend fun findUserName(email: String) {
        try {
            val code = userRepository.sendVerificationCode(email)
            generatedCode = code
            _authStatus.value = AuthStatus.Success(AuthAction.SendEmail)
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "이메일 발송 실패")
        }
    }

    suspend fun changePassword(email: String, userName: String) {
        try {
            Log.d("ChangePassword", "Checking existence for email: $email, username: $userName")
            val exists = userRepository.isUserExist(userName, email)
            Log.d("ChangePassword", "User existence result: $exists")

            if (exists) {
                Log.d("ChangePassword", "User exists. Sending verification code to $email.")
                generatedCode = userRepository.sendVerificationCode(email)
                _authStatus.value = AuthStatus.Success(AuthAction.SendEmail)
                Log.d("ChangePassword", "Verification code sent successfully.")
            } else {
                Log.d("ChangePassword", "User does not exist with provided email or username.")
                _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "이메일 또는 아이디가 잘못되었습니다.")
            }
        } catch (e: Exception) {
            Log.e("ChangePassword", "Error occurred during password change request: ${e.message}", e)
            _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "비밀번호 변경 요청 중 오류 발생")
        }
    }


    fun verifyCode(inputCode: String) {
        if (generatedCode == inputCode) {
            Log.d("VerifyCode", "인증 코드가 일치합니다.")
            _authStatus.value = AuthStatus.Success(AuthAction.VerifyCode)
        } else {
            Log.d("VerifyCode", "인증 코드가 일치하지 않습니다.")
            _authStatus.value = AuthStatus.Failure(AuthAction.VerifyCode, "인증 코드가 일치하지 않습니다.")
        }
    }

    suspend fun unregisterUser(context: Context, userDTO: UserDTO) {
        val userId = UserSessionManager.getUserId(context)
        try {
            val user = userRepository.getUserByUserId(userId)
            if (user != null && BCrypt.checkpw(userDTO.password, user.password)) {
                _authStatus.value = AuthStatus.Success(AuthAction.UNREGISTER)
            } else {
                _authStatus.value = AuthStatus.Failure(AuthAction.UNREGISTER, "비밀번호가 틀렸습니다.")
            }
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.UNREGISTER, "회원탈퇴 처리 중 오류 발생")
        }
    }
    private fun isValidUser(userDTO: UserDTO): Boolean {
        if (userDTO.userName.isNullOrEmpty() || userDTO.userName.length < 5) {
            Log.w("AuthViewModel", "Invalid userName: ${userDTO.userName}")
            return false
        }

        // 이메일 형식 검사
        val emailPattern = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
        if (userDTO.email.isNullOrEmpty() || !userDTO.email.matches(emailPattern)) {
            Log.w("AuthViewModel", "Invalid email: ${userDTO.email}")
            return false
        }

        // 비밀번호 검사
        if (userDTO.password.isNullOrEmpty() || userDTO.password.length < 8) {
            Log.w("AuthViewModel", "Invalid password: ${userDTO.password}")
            return false
        }
        return true
    }
    private fun hashPassword(password: String?): String {
        if (password.isNullOrEmpty()) {
            return "NO_PASSWORD" // 소셜 로그인 등 비밀번호가 없는 경우 기본값 설정
        }

        return try {
            BCrypt.hashpw(password, BCrypt.gensalt())
        } catch (e: Exception) {
            Log.e("HashPassword", "비밀번호 해싱 중 오류 발생: ${e.message}", e)
            throw RuntimeException("비밀번호 해싱에 실패했습니다.", e)
        }
    }


}
