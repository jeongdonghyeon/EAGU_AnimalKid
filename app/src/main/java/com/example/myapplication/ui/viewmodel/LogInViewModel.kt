package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LogInViewModel(private val userRepository: UserRepository): ViewModel(){
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun loginWithEmail(username: String, password: String){
        viewModelScope.launch {
            if(username.isNullOrEmpty() || password.isNullOrEmpty()){
                _loginStatus.value = false
            }
            try {
                val user = userRepository.getUserByUsername(username)
                if (user != null && BCrypt.checkpw(password,user.password) ) {
                    _loginStatus.value = true
                } else {
                    _loginStatus.value = false
                }
            } catch(e: Exception){
                _loginStatus.value = false
            }
        }
    }
    fun loginWithGoogle(userDTO: UserDTO){
        if(userDTO.email.isNullOrEmpty() || userDTO.userName.isNullOrEmpty()){
            _loginStatus.value = false
        }
        viewModelScope.launch {
            try{
                val isRegistered = userRepository.registerGoogleUser(userDTO)
                _loginStatus.value = true
            }catch(e: Exception){
                _loginStatus.value = false
            }
        }
    }

}