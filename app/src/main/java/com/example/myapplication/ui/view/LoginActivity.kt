package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.databinding.ActivityBottomBinding
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.Factory.AuthViewModelFactory
import com.example.myapplication.ui.viewmodel.state.AuthStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            authViewModel.handleGoogleSignInResult(result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupGoogleSignInClient()
        setupUI()
        observeAuthStatus()
    }

    private fun setupViewModel() {
        val userRepository = UserRepository(application)
        val factory = AuthViewModelFactory(userRepository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("GOOGLE_CLIENT_ID")
            .requestEmail()
            .build()
         googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupUI() {
        binding.passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                lifecycleScope.launch { loginUser() }
                true
            } else {
                false
            }
        }

        // 버튼 클릭 이벤트 설정
        binding.findIdButton.setOnClickListener {
            startActivity(Intent(this, findIdActivity::class.java))
        }
        binding.changePasswordButton.setOnClickListener {
            startActivity(Intent(this, changePasswordActivity::class.java))
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, registerActivity::class.java))
        }
        binding.googleRegisterButton.setOnClickListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun observeAuthStatus() {
        authViewModel.authStatus.observe(this) { authStatus ->
            when (authStatus) {
                is AuthStatus.Loading -> {
                    Toast.makeText(this, "로그인 중...", Toast.LENGTH_SHORT).show()
                }
                is AuthStatus.Success -> {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, UserProfileSetupActivity::class.java))
                }
                is AuthStatus.Failure -> {
                    Toast.makeText(this, authStatus.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun loginUser() {
        val username = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        authViewModel.login(username, password)
    }


}
