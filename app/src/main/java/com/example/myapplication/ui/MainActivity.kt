package com.example.myapplication.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.fragment.home
import com.example.myapplication.ui.view.TodoAdapter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // 날짜별 투두리스트를 저장할 데이터 모델
    private val todoMap = mutableMapOf<CalendarDay, MutableList<String>>()

    // RecyclerView Adapter 초기화
    private lateinit var todoAdapter: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        // MaterialCalendarView 설정 및 날짜 선택 리스너 추가
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendar_view)
        calendarView.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = CalendarDay.from(date.year, date.month, date.day)
            val todoList = todoMap[selectedDate] ?: mutableListOf("오늘의 할 일이 없습니다.")

            // Adapter에 투두리스트 설정하여 RecyclerView에 표시
            todoAdapter.updateData(todoList)
        }

        // RecyclerView 설정
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(mutableListOf())
        recyclerView.adapter = todoAdapter
    }

    // 투두리스트에 항목 추가
    fun addTodoItem(date: CalendarDay, todo: String) {
        if (todoMap[date] == null) {
            todoMap[date] = mutableListOf()
        }
        todoMap[date]?.add(todo)
    }

    private fun showAddGroupDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.addgroup)
        dialog.setCancelable(true)
        dialog.findViewById<ImageButton>(R.id.exitAddGroup).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
