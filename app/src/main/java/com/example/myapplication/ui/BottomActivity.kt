package com.example.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import androidx.activity.enableEdgeToEdge
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBottomBinding
import com.example.myapplication.ui.fragment.home
import com.example.myapplication.ui.fragment.profile
import com.example.myapplication.ui.fragment.reward
import com.example.myapplication.ui.fragment.walk

class BottomActivity : AppCompatActivity() {
    private val binding: ActivityBottomBinding by lazy {
        ActivityBottomBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기화면 -> 홈화면으로 설정함
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.frag_home
        }
    }

    fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.frag_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, home()).commit()
                    true
                }
                R.id.frag_walk -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, walk()).commit()
                    true
                }
                R.id.frag_reward -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, reward()).commit()
                    true
                }
                /*
                R.id.frag_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, profile()).commit()
                    true
                }
                 */
                else -> false
            }
        }
    }
}