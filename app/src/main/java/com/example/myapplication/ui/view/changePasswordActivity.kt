package com.example.myapplication.ui.view

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel

class changePasswordActivity : AppCompatActivity(){

    private lateinit var  binding: ActivityLoginBinding
    private lateinit var authViewModel : AuthViewModel


    private suspend fun emailVerification(){
        val email = binding.emailEditText.text.toString()
        val username = binding.emailEditText.text.toString()
        authViewModel.changePassword(email,username)
    }
    private suspend fun verifyCode(){
        val inputCode = binding.emailEditText.text.toString()
        authViewModel.verifyCode(inputCode)
    }

}