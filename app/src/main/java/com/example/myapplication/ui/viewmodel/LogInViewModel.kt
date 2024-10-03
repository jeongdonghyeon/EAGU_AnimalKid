package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.UserRepository
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LogInViewModel(private val userRepository: UserRepository): ViewModel(){
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun loginUser(username: String, password: String){
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByUsername(username)
                val bCryptPassword = BCrypt.hashpw(password,BCrypt.gensalt())
                val bCryptStoredPassword = BCrypt.hashpw(user?.password,BCrypt.gensalt())
                if (user != null && bCryptPassword == bCryptStoredPassword) {
                    _loginStatus.value = true
                } else {
                    _loginStatus.value = false
                }
            } catch(e: Exception){
                _loginStatus.value = false
            }
        }
    }
}