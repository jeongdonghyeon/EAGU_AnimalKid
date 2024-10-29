package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.entity.CalendarEntity
import com.example.myapplication.data.repository.CalendarRepository
import kotlinx.coroutines.launch

 class CalendarViewModel(private val repository: CalendarRepository) : ViewModel(){

     private val _events = MutableLiveData<List<CalendarEntity.CalendarEvent>>()
     val events: LiveData<List<CalendarEntity.CalendarEvent>> get() = _events

     fun insert(event: CalendarEntity.CalendarEvent){
         viewModelScope.launch {
             repository.insert(event)
         }
     }

        fun getAllEvents(date : String) {
                viewModelScope.launch{
                    _events.value = repository.getAll(date)
                }
        }

        fun update(event: CalendarEntity.CalendarEvent)=viewModelScope.launch {
            repository.update(event)
        }

        fun delete(event: CalendarEntity.CalendarEvent)=viewModelScope.launch {
            repository.delete(event)
        }
    }