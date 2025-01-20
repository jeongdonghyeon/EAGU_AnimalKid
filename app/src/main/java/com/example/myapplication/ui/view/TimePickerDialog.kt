package com.example.myapplication.ui.view


import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.util.Calendar

class TimePickerDialog {

    class MainActivity : AppCompatActivity() {

        @SuppressLint("MissingInflatedId", "WrongViewCast")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // 버튼을 찾아서 클릭 리스너 설정
            val selectTimeButton: Button = findViewById(R.id.startTimeText)
            val endTimeText: Button = findViewById(R.id.endTimeText)
            selectTimeButton.setOnClickListener {
                // 버튼 클릭 시 타임피커 표시
                showClockStyleTimePickerDialog()
            }
            endTimeText.setOnClickListener{
                showClockStyleTimePickerDialog()
            }
        }

        private fun showClockStyleTimePickerDialog() {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // TimePickerDialog 생성
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.THEME_HOLO_LIGHT, // 시계 모양으로 표시되는 테마 설정
                { _, selectedHour, selectedMinute ->
                    // 선택한 시간 표시
                    Toast.makeText(this, "선택한 시간: $selectedHour:$selectedMinute", Toast.LENGTH_SHORT).show()
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show() // 다이얼로그 표시
        }
    }

}