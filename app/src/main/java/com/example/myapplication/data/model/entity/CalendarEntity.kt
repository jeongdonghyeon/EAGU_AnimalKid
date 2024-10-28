package com.example.myapplication.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "events")
    data class CalendarEvent(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val title: String,
        val description: String,
        val date: String,  // YYYY-MM-DD 형식
        val time: String   // HH:mm 형식
    )