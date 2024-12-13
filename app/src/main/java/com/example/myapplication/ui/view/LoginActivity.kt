package com.example.myapplication.ui.view


import android.content.Context
import android.content.Intent
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
import com.example.myapplication.utils.PreferencesHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data: Intent? = result.data
        authViewModel.handleGoogleSignInResult(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                lifecycleScope.launch { loginUser(this@LoginActivity) }
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
                    val isLoginSuccessful = loginUser(this@LoginActivity)

                    if (isLoginSuccessful) {
                        // 로그인 성공 시 화면 전환
                        val intent = Intent(this@LoginActivity, AdultProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 실패 메시지 표시
                        when (val status = authViewModel.authStatus.value) {
                            is AuthStatus.Failure -> {
                                Toast.makeText(this@LoginActivity, status.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this@LoginActivity, "알 수 없는 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    // 예외 처리
                    Log.e("LoginButton", "로그인 중 오류 발생: ${e.message}", e)
                    Toast.makeText(this@LoginActivity, "오류가 발생했습니다. 다시 시도하세요.", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this,AdultProfileActivity::class.java))
        }
    }
    private fun observeAuthStatus() {
        authViewModel.authStatus.observe(this) { authStatus ->
            when (authStatus) {
                is AuthStatus.Loading -> {
                    Toast.makeText(this, "로그인 중...", Toast.LENGTH_SHORT).show()
                }
                is AuthStatus.Success -> {
                    // 상태 저장
                    PreferencesHelper.setLoggedIn(this, true)
                    PreferencesHelper.setProfileSetupComplete(this, false)

                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // LauncherActivity로 이동
                    startActivity(Intent(this, LauncherActivity::class.java))
                    finish()
                }
                is AuthStatus.Failure -> {
                    Toast.makeText(this, authStatus.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun loginUser(context: Context): Boolean {
        return try {
            val username = binding.UserIdEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            authViewModel.login(username, password, context)
        } catch (e: Exception) {
            Log.e("LoginActivity", "로그인 실패: ${e.message}")
            false
        }
    }



}
