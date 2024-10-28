package com.example.myapplication.ui

import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.repository.CalendarRepository
import com.example.myapplication.ui.viewmodel.CalendarViewModel

//!
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.calander)


        calendarView = findViewById(R.id.calendarView)

        // 날짜 변경 리스너 설정
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(applicationContext, "Selected date: $date", Toast.LENGTH_SHORT).show()
        }
    }

    //!

}
