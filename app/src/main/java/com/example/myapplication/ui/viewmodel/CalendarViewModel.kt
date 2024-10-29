package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.entity.CalendarEntity
import com.example.myapplication.data.repository.CalendarRepository
import kotlinx.coroutines.launch

class CalendarViewModel(private val repository: CalendarRepository) : ViewModel() {

    private val _events = MutableLiveData<List<CalendarEntity.CalendarEvent>>()
    val events: LiveData<List<CalendarEntity.CalendarEvent>> get() = _events

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun insert(event: CalendarEntity.CalendarEvent) {
        viewModelScope.launch {
            try {
                repository.insert(event)
                refreshEvents(event.date)
            } catch (e: Exception) {
                _error.postValue("이벤트 추가 실패: ${e.message}")
            }
        }
    }

    fun getAllEvents(date: String) {
        viewModelScope.launch {
            try {
                _events.postValue(repository.getAll(date))
            } catch (e: Exception) {
                _error.postValue("이벤트 조회 실패: ${e.message}")
            }
        }
    }

    fun update(event: CalendarEntity.CalendarEvent) {
        viewModelScope.launch {
            try {
                repository.update(event)
                refreshEvents(event.date)
            } catch (e: Exception) {
                _error.postValue("이벤트 업데이트 실패: ${e.message}")
            }
        }
    }

    fun delete(event: CalendarEntity.CalendarEvent) {
        viewModelScope.launch {
            try {
                repository.delete(event)
                refreshEvents(event.date)
            } catch (e: Exception) {
                _error.postValue("이벤트 삭제 실패: ${e.message}")
            }
        }
    }

    private fun refreshEvents(date: String) {
        getAllEvents(date)
    }
}