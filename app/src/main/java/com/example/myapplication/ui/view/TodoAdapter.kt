package com.example.myapplication.ui.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class TodoAdapter(private var todoList: MutableList<String>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoText: TextView = itemView.findViewById(R.id.todoText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.todoText.text = todoList[position]
    }

    override fun getItemCount(): Int = todoList.size

    // 데이터 업데이트 메서드
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<String>) {
        todoList.clear()
        todoList.addAll(newData)
        notifyDataSetChanged()
    }
}
