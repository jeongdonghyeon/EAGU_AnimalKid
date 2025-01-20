package com.example.myapplication.data.local
import androidx.room.*
import com.example.myapplication.data.model.entity.CalendarEvent

@Dao
    interface EventDao {
        @Insert
        suspend fun insertEvent(event: CalendarEvent)

        @Query("SELECT * FROM events")
        suspend fun getAllEvents(): List<CalendarEvent>

        @Update
        suspend fun updateEvent(event: CalendarEvent)

        @Delete
        suspend fun deleteEvent(event: CalendarEvent)
    }