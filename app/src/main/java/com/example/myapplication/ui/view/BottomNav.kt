package com.example.myapplication

import android.app.Activity
import androidx.fragment.app.FragmentManager
import com.example.myapplication.ui.view.fragment.profile
import com.example.myapplication.ui.view.fragment.reward
import com.example.myapplication.ui.view.fragment.walk
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.view.fragment.home

class BottomNav {

    // BottomNavigation 설정
    fun setupBottomNav(activity: Activity, binding: ActivityMainBinding, fragmentManager: FragmentManager) {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, home()).commit()
                    true
                }
                R.id.walkFragment -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, walk()).commit()
                    true
                }
                R.id.rewardFragment -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, reward()).commit()
                    true
                }
                R.id.profileFragment -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, profile()).commit()
                    true
                }
                else -> false
            }
        }
    }
}