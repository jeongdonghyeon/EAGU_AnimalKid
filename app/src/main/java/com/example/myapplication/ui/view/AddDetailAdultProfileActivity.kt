package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.DTO.ProfileDTO
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.databinding.AddDetailAdultProfileBinding
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import com.example.myapplication.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddDetailAdultProfileActivity : AppCompatActivity() {

    private lateinit var binding: AddDetailAdultProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding 초기화 후 setContentView 호출
        binding = AddDetailAdultProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("AddDetailProfile", "Activity created")

        // UserRepository 초기화
        userRepository = UserRepository(application)

        // UserId 가져오기
        userId = UserSessionManager.getUserId(this)
            ?: throw IllegalStateException("User ID not found in session")

        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        // 성별 스피너 설정
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.gender.adapter = genderAdapter

        // 생년월일 스피너 설정
        val years = (1950..2024).toList().map { it.toString() }
        val birthdateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        birthdateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.birthdate.adapter = birthdateAdapter
    }

    private fun setupListeners() {
        binding.authenticationButton.setOnClickListener { view ->
            Log.d("AddDetailProfile", "Authentication button clicked")

            // 입력 유효성 검사
            if (validateInputs()) {
                view.isEnabled = false // 중복 클릭 방지
                saveProfile()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.editTextText1.text.toString()
        val nickname = binding.editTextText2.text.toString()

        if (name.isEmpty()) {
            binding.editTextText1.error = "이름을 입력해주세요"
            return false
        }

        if (nickname.isEmpty()) {
            binding.editTextText2.error = "호칭을 입력해주세요"
            return false
        }

        return true
    }

    private fun saveProfile() {
        val name = binding.editTextText1.text.toString().trim()
        val nickname = binding.editTextText2.text.toString().trim()
        val gender = binding.gender.selectedItem?.toString() ?: ""
        val birthdate = binding.birthdate.selectedItem?.toString() ?: ""


        lifecycleScope.launch {
            try {
                val profileDTO = ProfileDTO(userId, name, nickname, gender, birthdate) // userId 포함
                userRepository.updateProfile(profileDTO)
                Log.d("SaveProfile", "Profile saved successfully")

                // 상태 업데이트 및 화면 전환
                updateProfileSetupStatus()
                navigateToMainScreen()
            } catch (e: Exception) {
                Log.e("SaveProfile", "Error saving profile", e)

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AddDetailAdultProfileActivity,
                        "프로필 저장 중 오류가 발생했습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.authenticationButton.isEnabled = true // 버튼 다시 활성화
                }
            }
        }
    }

    private fun updateProfileSetupStatus() {
        // SharedPreferences 업데이트를 PreferencesHelper로 처리
        PreferencesHelper.setProfileSetupComplete(this, true)
    }

    private fun navigateToMainScreen() {
        startActivity(Intent(this@AddDetailAdultProfileActivity, HomeActivity::class.java))
        finish()
    }
}

