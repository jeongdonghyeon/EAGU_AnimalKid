package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.databinding.ActivityBottomBinding
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.state.AuthStatus

class LoginActivity : AppCompatActivity(){

    private lateinit var  binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.passwordEditText.setOnEditorActionListener {_, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                loginUser()
                true
            }else{
                false
            }
        }
        authViewModel.authStatus.observe(this, Observer { authStatus ->
            when(authStatus) {
                is AuthStatus.Loading -> {
                    Toast.makeText(this,"로그인 중...",Toast.LENGTH_SHORT).show()
                }
                is AuthStatus.Success -> {
                    Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,UserProfileSetupActivity::class.java)
                    startActivity(intent)
                }
                is AuthStatus.Failure -> {
                    Toast.makeText(this,authStatus.message,Toast.LENGTH_SHORT).show()
                }

            }
        })
        binding.findIdButton.setOnClickListener{
            val intent = Intent(this,findIdActivity::class.java)
            startActivity(intent)
        }
        binding.changePasswordButton.setOnClickListener{
            val intent = Intent(this,changePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.registerButton.setOnClickListener{
            val intent = Intent(this,registerActivity::class.java)
            startActivity(intent)
        }
        binding.googleRegisterButton.setOnClickListener{
            val intent = Intent(this,googleRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(){
        val username = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val userDTO = UserDTO(userName = username, password = password)
        authViewModel.login(this,userDTO)
    }
}

