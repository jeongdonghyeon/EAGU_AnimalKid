package com.example.myapplication.ui.view

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel

class findIdActivity : AppCompatActivity(){

    private lateinit var  binding: ActivityLoginBinding
    private lateinit var authViewModel : AuthViewModel

    private suspend fun sendEmail(){
        val email = binding.emailEditText.text.toString()
        authViewModel.findUserName(email)
    }
    private suspend fun verifyCode(){
        val inputCode = binding.emailEditText.text.toString()
        authViewModel.verifyCode(inputCode)
    }
}