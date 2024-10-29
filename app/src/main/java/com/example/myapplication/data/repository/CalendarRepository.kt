package com.example.myapplication.data.repository

import com.example.myapplication.data.local.EventDao
import com.example.myapplication.data.model.entity.CalendarEvent

class CalendarRepository(private val eventDao: EventDao){

    suspend fun insert(event: CalendarEvent){
        eventDao.insertEvent(event)
    }

    suspend fun getAll(): List<CalendarEvent>{
        return eventDao.getAllEvents()
    }

    suspend fun update(event: CalendarEvent){
        eventDao.updateEvent(event)
    }

    suspend fun delete(event: CalendarEvent){
        eventDao.deleteEvent(event)
    }
}