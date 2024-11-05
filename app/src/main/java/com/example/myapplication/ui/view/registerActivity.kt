package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.databinding.FindIdBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.UUID

class registerActivity : AppCompatActivity(){

    private lateinit var  binding: ActivityRegisterBinding
    private lateinit var authViewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_register)

        //LoginButton버튼 클릭 후 보로자프로필 생성창으로 이동
//        LoginButton.setOnClickListener{
//            val intent = Intent(this, AdultProfileActivity::class.java)
//            startActivity(intent)
//        }
        binding.LoginButton.setOnClickListener{
            val intent = Intent(this, AdultProfileActivity::class.java)
            startActivity(intent)
        }

        setupToolbar()
        setupListeners()
    }
    // 툴바에 있는 뒤로가기 버튼을 클릭했을 때
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        binding.backBtn.setOnClickListener{
            finish()
        }
    }
    // 핸드폰  자체 기능인 뒤로가기 했을 때
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setupListeners(){
        binding.LoginButton.setOnClickListener{
            lifecycleScope.launch {
                registerUser()
            }
        }
    }

    private suspend fun registerUser(){
        val email = binding.IDedittext.text.toString()
        val username = binding.Emailedittext.text.toString()
        val password1 = binding.PWedittext.text.toString()
        val password2 = binding.PWcheckedittext.text.toString()

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