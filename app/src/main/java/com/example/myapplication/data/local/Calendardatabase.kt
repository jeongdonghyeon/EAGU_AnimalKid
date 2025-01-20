package com.example.myapplication.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.model.entity.CalendarEvent

class Calendardatabase {
    @Database(entities = [CalendarEvent::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun eventDao(): EventDao
    }

}