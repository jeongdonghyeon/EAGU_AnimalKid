package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.entity.CalendarEvent
import com.example.myapplication.data.repository.CalendarRepository
import kotlinx.coroutines.launch

 class CalendarViewModel(private val repository: CalendarRepository) : ViewModel(){

        fun insert(event:CalendarEvent)=viewModelScope.launch {
            repository.insert(event)
        }

        fun getAllEvents() = viewModelScope.launch {
            repository.getAll()
        }

        fun update(event: CalendarEvent)=viewModelScope.launch {
            repository.update(event)
        }

        fun delete(event: CalendarEvent)=viewModelScope.launch {
            repository.delete(event)
        }
    }