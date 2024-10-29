package com.example.myapplication.ui.viewmodel.Factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.ui.viewmodel.AuthViewModel

class AuthViewModelFactory(private val userRepository: UserRepository,private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}