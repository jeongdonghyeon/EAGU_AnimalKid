package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.utils.PreferencesHelper

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isLoggedIn = PreferencesHelper.isLoggedIn(this)
        val isProfileSetupComplete = PreferencesHelper.isProfileSetupComplete(this)

        when {
            isLoggedIn && isProfileSetupComplete -> {
                // 메인 홈 화면으로 이동
                startActivity(Intent(this, fragmentHomeActivity::class.java))
            }
            isLoggedIn -> {
                // 프로필 설정 화면으로 이동
                startActivity(Intent(this, AddDetailAdultProfileActivity::class.java))
            }
            else -> {
                // 로그인 화면으로 이동
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish() // LauncherActivity 종료
    }
}
