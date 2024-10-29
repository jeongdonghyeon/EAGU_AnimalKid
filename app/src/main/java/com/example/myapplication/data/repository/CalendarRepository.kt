package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.EventDao
import com.example.myapplication.data.model.entity.CalendarEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CalendarRepository(
    private val eventDao: EventDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    init {
        observeFireStoreEvents()
    }


    private fun observeFireStoreEvents() {
        firestore.collection("events")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                snapshots?.documents?.mapNotNull { it.toObject(CalendarEntity.CalendarEvent::class.java) }
                    ?.forEach { event ->
                        CoroutineScope(Dispatchers.IO).launch {
                            eventDao.insertEvent(event)
                        }
                    }
            }
    }

    private suspend fun addEventToFireStore(event: CalendarEntity.CalendarEvent) = try {
        firestore.collection("events").add(event).await()
        Log.d("Firestore", "이벤트가 성공적으로 추가했습니다.")
    } catch (e: Exception) {
        Log.w("Firestore", "추가가 실패되었습니다.", e)
    }


    private suspend fun updateEventInFireStore(event: CalendarEntity.CalendarEvent) = try {
        firestore.collection("events").document(event.id.toString()).set(event.toMap()).await()
        Log.d("Firestore", "이벤트가 성공적으로 업데이트했습니다.")
    } catch (e: Exception) {
        Log.w("Firestore", "업데이트가 실패되었습니다.", e)
    }


    private suspend fun deleteEventFromFireStore(event: CalendarEntity.CalendarEvent) = try {
        firestore.collection("events").document(event.id.toString()).delete().await()
        Log.d("Firestore", "이벤트가 성공적으로 삭제했습니다.")
    } catch (e: Exception) {
        Log.w("Firestore", "삭제가 실패되었습니다.", e)
    }


    suspend fun insert(event: CalendarEntity.CalendarEvent) {
        withContext(Dispatchers.IO) {
            eventDao.insertEvent(event)
            addEventToFireStore(event)
        }
    }


    suspend fun getAll(date: String): List<CalendarEntity.CalendarEvent> = withContext(Dispatchers.IO) {
        eventDao.getAllEvents(date)
    }


    suspend fun update(event: CalendarEntity.CalendarEvent) {
        withContext(Dispatchers.IO) {
            eventDao.updateEvent(event)
            updateEventInFireStore(event)
        }
    }


    suspend fun delete(event: CalendarEntity.CalendarEvent) {
        withContext(Dispatchers.IO) {
            eventDao.deleteEvent(event)
            deleteEventFromFireStore(event)
        }
    }


    private fun CalendarEntity.CalendarEvent.toMap() = mapOf(
        "title" to title,
        "description" to description,
        "date" to date,
        "time" to time
    )
}