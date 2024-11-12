package com.example.myapplication.ui.view

import KEY_PROFILE_SETUP_COMPLETE
import PREFS_NAME
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.databinding.AddDetailAdultProfileBinding
import kotlinx.coroutines.launch

class AddDetailAdultProfileActivity : AppCompatActivity() {

    private lateinit var binding: AddDetailAdultProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userId: String // 로그인 후 전달받은 userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddDetailAdultProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(application)

        // 성별 선택을 위한 스피너 설정
        val genderOptions = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this@AddDetailAdultProfileActivity, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// EditText가 아닌 Spinner의 ID를 사용하여 어댑터를 설정합니다.
        binding.gender.adapter = adapter


        // 프로필 저장 버튼 클릭 시
        binding.authenticationButton.setOnClickListener {
            saveProfile()
        }

        // 프로필 설정 완료 버튼 클릭 시
        binding.authenticationButton.setOnClickListener {
            // 프로필 설정 완료 로직 처리

            // 프로필 설정 완료 상태 저장
            val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(KEY_PROFILE_SETUP_COMPLETE, true)
            editor.apply()

            // 메인 홈 화면으로 이동
            startActivity(Intent(this, fragmentHomeActivity::class.java))
            finish() // 프로필 액티비티 종료
        }
    }

    private fun saveProfile() {
        // 입력된 프로필 정보 가져오기
        val name = binding.editTextText1.text.toString()
        val nickname = binding.editTextText2.text.toString()
        val gender = binding.gender.selectedItem.toString()
        val birthdate = binding.birthdate.selectedItem.toString()

        // 비동기적으로 데이터베이스에 프로필 정보 저장
        lifecycleScope.launch {
            userRepository.updateProfile(userId, name, nickname, gender, birthdate)

            // 프로필 저장 후 메인 화면으로 이동
            val intent = Intent(this@AddDetailAdultProfileActivity, fragmentHomeActivity::class.java)
            startActivity(intent)
            finish() // 현재 Activity 종료
        }
    }
}
