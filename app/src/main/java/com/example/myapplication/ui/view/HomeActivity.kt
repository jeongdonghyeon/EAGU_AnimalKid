package com.example.myapplication.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.viewmodel.Session.UserSessionManager
import com.example.myapplication.ui.view.fragment.home
import com.example.myapplication.ui.view.fragment.walk
import com.example.myapplication.ui.view.fragment.reward
import com.example.myapplication.ui.view.fragment.profile

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = UserSessionManager.getUserId(this)
            ?: throw IllegalStateException("User ID not found in session")

        setupListeners()
    }

    private fun setupListeners() {
        // Set default fragment
        navigateToFragment(home())

        // Handle BottomNavigationView item selection
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navigateToFragment(home())
                    true
                }
                R.id.nav_walk -> {
                    navigateToFragment(walk())
                    true
                }
                R.id.nav_reward -> {
                    navigateToFragment(reward())
                    true
                }
                R.id.nav_profile -> {
                    navigateToFragment(profile())
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}
