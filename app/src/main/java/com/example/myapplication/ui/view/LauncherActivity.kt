package com.example.myapplication.ui.view

import KEY_IS_LOGGED_IN
import KEY_PROFILE_SETUP_COMPLETE
import PREFS_NAME
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)

        //ShaerePreferences불러오기
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        val isProfileSetupComplete = sharedPreferences.getBoolean(KEY_PROFILE_SETUP_COMPLETE, false)

        if (isLoggedIn) {
            if (isProfileSetupComplete) {
                // 로그인 & 프로필 설정 완료 - 메인 홈 화면으로 이동
                startActivity(Intent(this, fragmentHomeActivity::class.java))
            } else {
                // 로그인했으나 프로필 설정 미완료 - 프로필 설정 화면으로 이동
                startActivity(Intent(this, CreateAdultProfileActivity::class.java))
            }
        } else {
            // 로그인되지 않은 경우 - 로그인 화면으로 이동
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish() // 이 액티비티 종료

    }
}