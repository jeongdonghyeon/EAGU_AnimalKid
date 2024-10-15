package com.example.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eagu_animalkid_test.profile
import com.example.eagu_animalkid_test.reward
import com.example.eagu_animalkid_test.walk
//import androidx.activity.enableEdgeToEdge
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBottomBinding
import com.example.myapplication.ui.fragment.home

class BottomActivity : AppCompatActivity() {
    private val binding: ActivityBottomBinding by lazy {
        ActivityBottomBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()

        window.statusBarColor = ContextCompat.getColor(this, R.color.SystemUIColor)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.SoftkeyColor)

        // 앱 초기화면 -> 홈화면으로 설정함
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.homeFragment
        }
    }

    fun setBottomNavigationView() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, home()).commit()
                    true
                }
                R.id.walkFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, walk()).commit()
                    true
                }
                R.id.rewardFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, reward()).commit()
                    true
                }

                R.id.profileFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, profile()).commit()
                    true
                }
                else -> false
            }
        }
    }
}