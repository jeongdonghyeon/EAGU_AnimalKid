package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.CreateAdultProfileBinding

class CreateAdultProfileActivity : AppCompatActivity() {

    private lateinit var binding: CreateAdultProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAdultProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 버튼 클릭 시 AddDetailAdultProfileActivity로 이동
        binding.addButton.setOnClickListener {
            // AddDetailAdultProfileActivity로 이동
            val intent = Intent(this, AddDetailAdultProfileActivity::class.java)
            startActivity(intent)
            finish() // 현재 Activity 종료
        }
    }
}
