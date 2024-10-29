package com.example.myapplication.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "events")
    data class CalendarEvent(
        @PrimaryKey(autoGenerate = true)
        val Calendarid: Int = 0,
        val Calendartitle: String,
        val Calendardescription: String,
        val Calendardate: String,  // YYYY-MM-DD 형식
        val Calendartime: String   // HH:mm 형식
    )