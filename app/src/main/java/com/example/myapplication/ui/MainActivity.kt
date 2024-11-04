package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.fragment.home

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, R.color.SystemUIColor)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.SoftkeyColor)

        val bottomNav = BottomNav()
        bottomNav.setupBottomNav(this, binding, supportFragmentManager)

        val addButton: ImageButton = findViewById(R.id.AddButton)
        addButton.setOnClickListener {
            showAddGroupDialog()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, home())
                .commit()
        }
    }

    private fun showAddGroupDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.addgroup)
        dialog.setCancelable(true)
        dialog.findViewById<ImageButton>(R.id.exitAddGroup).setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
}