package com.example.myapplication.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import com.example.myapplication.ui.viewmodel.state.AuthAction
import com.example.myapplication.ui.viewmodel.state.AuthStatus

import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID

class AuthViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _authStatus = MutableLiveData<AuthStatus>()
    val authStatus: LiveData<AuthStatus> get() = _authStatus
    private var generatedCode: String? = null

    suspend fun login(userName : String, password : String) {
        _authStatus.value = AuthStatus.Loading(AuthAction.LOGIN)
        viewModelScope.launch {
            // 유저가 입력한 아이디와 비밀번호가 비어있는지 확인함
            if (userName.isEmpty() || password.isEmpty()) {
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "아이디와 비밀번호를 다시 입력해주세요")
                return@launch
            }
            //해당 유저가 있는지 있다면 입력한 비밀번호와 해당 유저의 비밀번호가 같은지 판별
            try {
                val user = userRepository.getUserByUsername(userName)
                if (user != null && BCrypt.checkpw(password, user.password)) {
                    _authStatus.value = AuthStatus.Success(AuthAction.LOGIN)
                } else {
                    _authStatus.value =
                        AuthStatus.Failure(AuthAction.LOGIN, "아이디 또는 비밀번호가 잘못되었습니다.")
                }
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "오류.")
            }
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            _authStatus.value = AuthStatus.Loading(AuthAction.LOGOUT)
            try {
                UserSessionManager.clearUserSession(context)
                _authStatus.value = AuthStatus.Success(AuthAction.LOGOUT)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGOUT, "로그아웃 실패")
            }
        }

    }

    suspend fun registerUser(userDTO: UserDTO,) {
        if (!isValidUser(userDTO)) {
            _authStatus.value =
                AuthStatus.Failure(AuthAction.REGISTER, "이메일 아이디 또는 비밀번호 중에 유효 하지 않는 것이 있습니다.")
            return
        }
        _authStatus.value = AuthStatus.Loading(AuthAction.REGISTER)

        viewModelScope.launch {
            try {
                userRepository.registerUser(userDTO)
                _authStatus.value = AuthStatus.Success(AuthAction.REGISTER)
            } catch (e: Exception) {
                _authStatus.value = AuthStatus.Failure(AuthAction.REGISTER, "에러, 다시 입력해주세요.")
            }
        }
    }

    suspend fun findUserName(email: String){
        viewModelScope.launch{
            val code = userRepository.senVerificationCode(email)
            generatedCode = code
            _authStatus.value  = AuthStatus.Success(AuthAction.SendEmail)
        }
    }
    suspend fun changePassword(email: String,userName: String){
        viewModelScope.launch {
            val username = userName
            val email = email
            val isUser = userRepository.isUserExist(username, email)
            if(isUser){
                val code = userRepository.senVerificationCode(email)
                generatedCode = code
                _authStatus.value  = AuthStatus.Success(AuthAction.SendEmail)
            }else{
                _authStatus.value = AuthStatus.Failure(AuthAction.SendEmail, "이메일 또는 아이디가 잘못되었습니다.")
            }
        }
    }
    fun verifyCode(inputCode: String){
        if(generatedCode == inputCode){
            _authStatus.value = AuthStatus.Success(AuthAction.VerifyCode)
        }else{
            _authStatus.value = AuthStatus.Failure( AuthAction.VerifyCode,"인증 코드가 일치하지 않습니다.")
        }
    }

    suspend fun unregisterUser(context: Context, userDTO: UserDTO) {
        val userId = UserSessionManager.getUserId(context)
        val user = userRepository.getUserByUserId(userId);
        viewModelScope.launch {
            if (user != null) {
                val inputPassword = userDTO.password
                if (BCrypt.checkpw(inputPassword, user.password)) {
                    _authStatus.value = AuthStatus.Success(AuthAction.UNREGISTER)
                } else {
                    _authStatus.value = AuthStatus.Failure(AuthAction.UNREGISTER, "비밀번호가 틀렸습니다.")
                }

            }
        }
    }

    private fun isValidUser(userDTO:UserDTO): Boolean {
        return (userDTO.userName.isNotEmpty() && userDTO.userName.matches("^[a-zA-Z0-9]{5,20}$".toRegex())
                && userDTO.email!!.isNotEmpty() && userDTO.email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex())
                && !userDTO.password.isNullOrEmpty() && userDTO.password.length >= 8)
    }


    /* view 에서 구글로부터 username, email 받아와야함

    fun loginWithGoogle(account: GoogleLoginAccount?){
        if(account == null){
            _loginStatus.value = RegistrationResult.Failure("구글 로그인에 실패했습니다.")
        }
        val userDTO = UserDTO(
            userName = account.userName,
            email = account.email,
        )
        viewModelScope.launch {
            try{
                val isRegistered = userRepository.registerGoogleUser(userDTO)
                _loginStatus.value = RegistrationResult.Success
            }catch(e: Exception){
                _loginStatus.value =  RegistrationResult.Failure("구글 로그인에 실패했습니다.")
            }
        }
    }
*/

}