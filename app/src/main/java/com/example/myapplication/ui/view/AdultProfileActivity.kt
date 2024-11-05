package com.example.myapplication.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.Adultprofile1Binding

class AdultProfileActivity : AppCompatActivity() {

    private lateinit var binding: Adultprofile1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Adultprofile1Binding.inflate(layoutInflater)  //binding 초기화
        setContentView(binding.root)

        //보호자 추가 버튼 클릭 후 보호자 추가 화면 이동
//        btnAddGuardian.setOnClickListener{
//            val intent=Intent(this, AdultProfileActivity::class.java)
//            startActivity(intent)
//        }
        binding.btnAddGuardian.setOnClickListener {
            val intent = Intent(this,AdultProfileActivity::class.java)
            startActivity(intent)
        }

    }
}