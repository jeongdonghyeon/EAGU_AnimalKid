package com.example.myapplication.data.repository

import com.example.myapplication.data.local.EventDao
import com.example.myapplication.data.model.entity.CalendarEntity

class CalendarRepository(private val eventDao: EventDao){

    suspend fun insert(event: CalendarEntity.CalendarEvent){
        eventDao.insertEvent(event)
    }

    suspend fun getAll(): List<CalendarEntity.CalendarEvent>{
        return eventDao.getAllEvents()
    }

    suspend fun update(event: CalendarEntity.CalendarEvent){
        eventDao.updateEvent(event)
    }

    suspend fun delete(event: CalendarEntity.CalendarEvent){
        eventDao.deleteEvent(event)
    }
}