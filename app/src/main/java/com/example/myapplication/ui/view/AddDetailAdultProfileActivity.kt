package com.example.myapplication.ui.view

import KEY_PROFILE_SETUP_COMPLETE
import PREFS_NAME
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.DTO.ProfileDTO
import com.example.myapplication.data.model.entity.ProfileEntity
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.databinding.AddDetailAdultProfileBinding
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddDetailAdultProfileActivity : AppCompatActivity() {

    private lateinit var binding: AddDetailAdultProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userId: String

    companion object {
        private const val PREFS_NAME = "ProfilePrefs"
        private const val KEY_PROFILE_SETUP_COMPLETE = "profile_setup_complete"
    }

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

        // 생년월일 스피너 설정 (예시 - 실제 구현에 맞게 수정 필요)
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
                view.isEnabled = false  // 중복 클릭 방지
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

        Log.d("SaveProfile",
            "Attempting to save - Name: $name, Nickname: $nickname, Gender: $gender, Birthdate: $birthdate")

        lifecycleScope.launch {
            try {
                val profileDTO = ProfileDTO(userId, name, nickname, gender, birthdate) // userId 포함
                userRepository.updateProfile(profileDTO)
                Log.d("SaveProfile", "Profile saved successfully")

                updateProfileSetupStatus() // 상태 업데이트
                navigateToMainScreen() // 화면 전환
            } catch (e: Exception) {
                Log.e("SaveProfile", "Error saving com.example.myapplication.ui.fragment.profile", e)

                // 오류 발생 시 UI 업데이트
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddDetailAdultProfileActivity,
                        "프로필 저장 중 오류가 발생했습니다",
                        Toast.LENGTH_SHORT).show()
                    binding.authenticationButton.isEnabled = true  // 버튼 다시 활성화
                }
            }
        }
    }

    private fun updateProfileSetupStatus() {
        // SharedPreferences 업데이트
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().apply {
            putBoolean(KEY_PROFILE_SETUP_COMPLETE, true)
            apply()
        }
    }

    private fun navigateToMainScreen() {
        startActivity(Intent(this@AddDetailAdultProfileActivity, MainActivity::class.java))
        finish()
    }

}
