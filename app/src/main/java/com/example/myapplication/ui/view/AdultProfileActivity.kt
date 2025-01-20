package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.CreateAdultProfileBinding

class AdultProfileActivity : AppCompatActivity() {

    private lateinit var binding: CreateAdultProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAdultProfileBinding.inflate(layoutInflater)  //binding 초기화
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            val intent = Intent(this,AddDetailAdultProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}