package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.viewmodel.state.RegistrationResult
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository): ViewModel(){

    private val _registrationStatus = MutableLiveData<RegistrationResult>()
    val registrationStatus: LiveData<RegistrationResult> get() = _registrationStatus

    fun registerUser(userDTO: UserDTO){
        if(!isValidUser(userDTO)){
            _registrationStatus.value = RegistrationResult.Failure("이메일, 아이디 또는 비밀번호 중에 유효 하지 않는 것이 있습니다.")
            return
        }
        viewModelScope.launch {
            try {
                userRepository.registerUser(userDTO)
                _registrationStatus.value = RegistrationResult.Success
            } catch (e: Exception){
                _registrationStatus.value = RegistrationResult.Failure("에러, 다시 입력해주세요.")
            }
        }
    }

    private fun isValidUser(userDTO:UserDTO): Boolean {
        return (!userDTO.userName.isNullOrEmpty() && userDTO.userName.matches("^[a-zA-Z0-9]{5,20}$".toRegex())
                && !userDTO.email.isNullOrEmpty() && userDTO.email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex())
                && !userDTO.password.isNullOrEmpty() && userDTO.password.length >= 8)
    }
}