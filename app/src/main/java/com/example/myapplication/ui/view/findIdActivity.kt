package com.example.myapplication.ui.view

import android.os.Bundle
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.example.myapplication.databinding.FindIdBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


class findIdActivity : AppCompatActivity(){


    private lateinit var binding: FindIdBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }
    // 툴바에 있는 뒤로가기 버튼을 클릭했을 때
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        binding.back.setOnClickListener{
            finish()
        }
    }
    // 핸드폰  자체 기능인 뒤로가기 했을 때
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setupListeners(){
        binding.requestNumber.setOnClickListener{
            lifecycleScope.launch {
                sendEmail()
            }
        }
        binding.authenticationButton.setOnClickListener{
            lifecycleScope.launch {
                verifyCode()
            }
        }
    }

    private suspend fun sendEmail(){
        val email = binding.enterEmail.text.toString()
        authViewModel.findUserName(email)
    }
    private suspend fun verifyCode(){
        val inputCode = binding.enterAuthentication.text.toString()
        authViewModel.verifyCode(inputCode)
    }
}