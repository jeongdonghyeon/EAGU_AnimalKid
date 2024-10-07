package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.viewmodel.state.RegistrationResult

import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LogInViewModel(private val userRepository: UserRepository): ViewModel(){
    private val _loginStatus = MutableLiveData<RegistrationResult>()
    val loginStatus: LiveData<RegistrationResult> get() = _loginStatus

    fun loginWithEmail(userDTO: UserDTO){
        viewModelScope.launch {
            if(userDTO.userName.isEmpty() || userDTO.password.isNullOrEmpty()){
                _loginStatus.value = RegistrationResult.Failure("아이디와 비밀번호를 입력해주세요")
            }
            try {
                val user = userRepository.getUserByEmail(userDTO)
                if (user != null && BCrypt.checkpw(userDTO.userName,user.password) ) {
                    _loginStatus.value = RegistrationResult.Success
                } else {
                    _loginStatus.value = RegistrationResult.Failure("아이디 또는 비밀번호가 잘못되었습니다.")
                }
            } catch(e: Exception){
                _loginStatus.value = RegistrationResult.Failure("에러, 다시 입력해주세요.")
            }
        }
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