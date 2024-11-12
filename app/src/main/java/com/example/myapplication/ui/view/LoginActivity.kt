package com.example.myapplication.ui.view

import KEY_IS_FIRST_LOGIN
import KEY_IS_LOGGED_IN
import KEY_PROFILE_SETUP_COMPLETE
import PREFS_NAME
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.viewmodel.AuthViewModel
import com.example.myapplication.ui.viewmodel.Factory.AuthViewModelFactory
import com.example.myapplication.ui.viewmodel.state.AuthStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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

        binding.LoginButton.setOnClickListener{
            // 로그인 성공 로직 처리

            val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(KEY_IS_LOGGED_IN, true)

            // 최초 로그인 여부 확인 및 프로필 설정 화면으로 이동
            val isFirstLogin = sharedPreferences.getBoolean(KEY_IS_FIRST_LOGIN, true)
            if (isFirstLogin) {
                editor.putBoolean(KEY_IS_FIRST_LOGIN, false)
                editor.apply()

                // 프로필 설정 화면으로 이동
                startActivity(Intent(this, CreateAdultProfileActivity::class.java))
            } else {
                editor.apply()

                // 이전에 로그인했고 프로필 설정 완료 상태에 따라 화면 결정
                val isProfileSetupComplete = sharedPreferences.getBoolean(KEY_PROFILE_SETUP_COMPLETE, false)
                if (isProfileSetupComplete) {
                    startActivity(Intent(this, fragmentHomeActivity::class.java))
                } else {
                    startActivity(Intent(this, CreateAdultProfileActivity::class.java))
                }
            }
            finish()    //로그인 맥티비티 종료
        }

        setupViewModel()
        setupGoogleSignInClient()
        setupUI()
        setupListeners()
        observeAuthStatus()
    }

    private fun setupViewModel() {
        val userRepository = UserRepository(application)
        val factory = AuthViewModelFactory(userRepository,application)
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

    }
    private fun setupListeners() {

        binding.LoginButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    // 로그인 함수 호출
                    loginUser()

                    // 로그인 후 AdultProfileActivity로 이동
                    val intent = Intent(this@LoginActivity, AdultProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Log.e("LoginButton", "로그인 중 오류 발생: ${e.message}", e)
                }
            }
        }
        binding.findIdButton.setOnClickListener {
            startActivity(Intent(this, findIdActivity::class.java))
        }
        binding.changePasswordButton.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
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
        val username = binding.UserIdEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        authViewModel.login(username, password)
    }


}
