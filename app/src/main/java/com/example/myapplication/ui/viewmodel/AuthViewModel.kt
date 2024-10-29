package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt


class AuthViewModel(private val userRepository: UserRepository,
                    private val application: Application) : ViewModel() {

    private val _authStatus = MutableLiveData<AuthStatus>()
    val authStatus: LiveData<AuthStatus> get() = _authStatus
    private var generatedCode: String? = null

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("GOOGLE_CLIENT_ID")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(application, gso)
    }
    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken ?: throw ApiException(Status.RESULT_INTERNAL_ERROR)
            val email = account.email ?: throw ApiException(Status.RESULT_INTERNAL_ERROR)
            val userDTO = UserDTO(userId = idToken, email = email)

            viewModelScope.launch {
                userRepository.registerUser(userDTO)
                _authStatus.value = AuthStatus.Success(AuthAction.LOGIN)
            }
        } catch (e: ApiException) {
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN,"Google 로그인 실패: ${e.message}")
        }
    }
    suspend fun login(userName: String, password: String) {
        _authStatus.value = AuthStatus.Loading(AuthAction.LOGIN)

        if (userName.isEmpty() || password.isEmpty()) {
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "아이디와 비밀번호를 다시 입력해주세요")
            return
        }

        try {
            val user = userRepository.getUserByUsername(userName)
            if (user != null && BCrypt.checkpw(password, user.password)) {
                _authStatus.value = AuthStatus.Success(AuthAction.LOGIN)
            } else {
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "아이디 또는 비밀번호가 잘못되었습니다.")
            }
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "오류가 발생했습니다.")
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

    suspend fun registerUser(userDTO: UserDTO) {
        if (!isValidUser(userDTO)) {
            _authStatus.value = AuthStatus.Failure(AuthAction.REGISTER, "유효하지 않은 이메일, 아이디 또는 비밀번호입니다.")
            return
        }
        _authStatus.value = AuthStatus.Loading(AuthAction.REGISTER)

        try {
            userRepository.registerUser(userDTO)
            _authStatus.value = AuthStatus.Success(AuthAction.REGISTER)
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.REGISTER, "회원가입 중 에러가 발생했습니다.")
        }
    }

    suspend fun findUserName(email: String) {
        try {
            val code = userRepository.senVerificationCode(email)
            generatedCode = code
            _authStatus.value = AuthStatus.Success(AuthAction.SendEmail)
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "이메일 발송 실패")
        }
    }

    suspend fun changePassword(email: String, userName: String) {
        try {
            if (userRepository.isUserExist(userName, email)) {
                generatedCode = userRepository.senVerificationCode(email)
                _authStatus.value = AuthStatus.Success(AuthAction.SendEmail)
            } else {
                _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "이메일 또는 아이디가 잘못되었습니다.")
            }
        } catch (e: Exception) {
            _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "비밀번호 변경 요청 중 오류 발생")
        }
    }

    fun verifyCode(inputCode: String) {
        _authStatus.value = if (generatedCode == inputCode) {
            AuthStatus.Success(AuthAction.VerifyCode)
        } else {
            AuthStatus.Failure(AuthAction.VerifyCode, "인증 코드가 일치하지 않습니다.")
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
        return userDTO.userName?.isNotEmpty() == true &&
                userDTO.userName.matches("^[a-zA-Z0-9]{5,20}$".toRegex()) &&
                userDTO.email?.isNotEmpty() == true &&
                userDTO.email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()) &&
                !userDTO.password.isNullOrEmpty() && userDTO.password.length >= 8
    }
}
