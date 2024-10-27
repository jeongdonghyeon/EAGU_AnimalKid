package com.example.myapplication.ui.view

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import com.google.android.material.snackbar.Snackbar
import java.util.UUID

class registerActivity : AppCompatActivity(){

    private lateinit var  binding: ActivityLoginBinding
    private lateinit var authViewModel : AuthViewModel









    private suspend fun registerUser(){
        val email = binding.emailEditText.text.toString()
        val username = binding.emailEditText.text.toString()
        val password1 = binding.passwordEditText.text.toString()
        val password2 = binding.passwordEditText.text.toString()

        if(password1 == password2){

            val userId = UUID.randomUUID().toString()
            UserSessionManager.saveUserId(this@registerActivity, userId)

            val userDTO = UserDTO(userId = userId,
                email = email,
                userName = username,
                password = password1)

            authViewModel.registerUser(userDTO)
        }else{
            Snackbar.make(binding.root, "비밀번호가 서로 일치하지 않습니다.", Snackbar.LENGTH_SHORT).show()        }
    }
}