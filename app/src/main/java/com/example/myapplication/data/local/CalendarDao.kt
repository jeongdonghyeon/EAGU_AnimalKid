package com.example.myapplication.data.local
import androidx.room.*
import com.example.myapplication.data.model.entity.CalendarEntity

@Dao
    interface EventDao {
        @Insert
        suspend fun insertEvent(event: CalendarEntity.CalendarEvent)

        @Query("SELECT * FROM events")
        suspend fun getAllEvents(): List<CalendarEntity.CalendarEvent>

        @Update
        suspend fun updateEvent(event: CalendarEntity.CalendarEvent)

        @Delete
        suspend fun deleteEvent(event: CalendarEntity.CalendarEvent)
    }