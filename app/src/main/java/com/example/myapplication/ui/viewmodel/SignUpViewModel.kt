package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository): ViewModel(){

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    fun registerUser(userDTO: UserDTO){
        viewModelScope.launch {
            try {
                userRepository.registerUser(userDTO)
                _registrationStatus.value = true
            } catch (e: Exception){
                _registrationStatus.value = false
            }
        }
    }
}