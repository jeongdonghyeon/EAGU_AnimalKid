package com.example.myapplication.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.FindIdBinding
import com.example.myapplication.databinding.FindPwBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.Factory.AuthViewModelFactory
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: FindPwBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindPwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRepository = UserRepository(application) // 실제 UserRepository 인스턴스를 제공
        val factory = AuthViewModelFactory(userRepository, application)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        setupToolbar()
        setupListeners()
    }

    // 툴바에 있는 뒤로가기 버튼을 클릭했을 때
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    // 핸드폰 자체 기능인 뒤로가기 했을 때
    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setupListeners() {
        binding.requestNumber.setOnClickListener {
            lifecycleScope.launch {
                emailVerification()
            }
        }
        binding.rerequestNumber.setOnClickListener {
            lifecycleScope.launch {
                emailVerification()
            }
        }
        binding.authenticationButton.setOnClickListener {
            lifecycleScope.launch {
                verifyCode()
            }
        }
    }

    private suspend fun emailVerification() {
        val email = binding.enterEmail.text.toString()
        val username = binding.enterId.text.toString()
        authViewModel.changePassword(email, username)
    }

    private suspend fun verifyCode() {
        val inputCode = binding.enterAuthentication.text.toString()
        authViewModel.verifyCode(inputCode)
    }
}