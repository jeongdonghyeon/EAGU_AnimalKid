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

class AuthViewModel(private val userRepository: UserRepository): ViewModel(){
    private val _authStatus = MutableLiveData<AuthStatus>()
    val authStatus: LiveData<AuthStatus> get() = _authStatus

    fun login(context: Context, userDTO: UserDTO){
        _authStatus.value = AuthStatus.Loading(AuthAction.LOGIN)
        viewModelScope.launch {
            // 유저가 입력한 아이디와 비밀번호가 비어있는지 확인함
            if(userDTO.userName.isEmpty() || userDTO.password.isNullOrEmpty()){
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN,"아이디와 비밀번호를 다시 입력해주세요")
                return@launch
            }
            //해당 유저가 있는지 있다면 입력한 비밀번호와 해당 유저의 비밀번호가 같은지 판별
            try {
                val user = userRepository.getUserByUsername(userDTO)
                if (user != null && BCrypt.checkpw(userDTO.password,user.password) ) {
                    _authStatus.value = AuthStatus.Success(AuthAction.LOGIN)
                } else {
                    _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN, "아이디 또는 비밀번호가 잘못되었습니다.")
                }
            } catch(e: Exception){
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGIN,"네트워크 오류.")
            }
        }
    }
    fun logout(context: Context){
        viewModelScope.launch {
            _authStatus.value = AuthStatus.Loading(AuthAction.LOGOUT)
            try {
                UserSessionManager.clearUserSession(context)
                _authStatus.value = AuthStatus.Success(AuthAction.LOGOUT)
            } catch(e : Exception){
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGOUT,"로그아웃 실패")
            }
        }

    }
    fun registerUser(context: Context,userDTO: UserDTO){
        if(!isValidUser(userDTO)){
            _authStatus.value = AuthStatus.Failure(AuthAction.LOGOUT,"이메일 아이디 또는 비밀번호 중에 유효 하지 않는 것이 있습니다.")
            return
        }
        viewModelScope.launch {
            try {
                val userId = UUID.randomUUID().toString()
                UserSessionManager.saveUserId(context,userId)
                val newUserDTO = userDTO.copy(userId = userId)
                userRepository.registerUser(newUserDTO)

            } catch (e: Exception){
                _authStatus.value = AuthStatus.Failure(AuthAction.LOGOUT,"에러, 다시 입력해주세요.")
            }
        }
    }
    private fun isValidUser(userDTO:UserDTO): Boolean {
        return (userDTO.userName.isNotEmpty() && userDTO.userName.matches("^[a-zA-Z0-9]{5,20}$".toRegex())
                && userDTO.email.isNotEmpty() && userDTO.email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex())
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