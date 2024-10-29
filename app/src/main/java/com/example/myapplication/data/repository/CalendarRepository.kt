package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.EventDao
import com.example.myapplication.data.model.entity.CalendarEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarRepository(private val eventDao: EventDao,
                         private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()){

    fun syncEventsWithFireStore() {
        firestore.collection("events")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                snapshots?.let {
                    for (doc in it.documents) {
                        val event = doc.toObject(CalendarEntity.CalendarEvent::class.java)
                        event?.let { eventData ->
                            CoroutineScope(Dispatchers.IO).launch {
                                eventDao.insertEvent(eventData)
                            }
                        }
                    }
                }
            }
    }
    suspend fun insert(event: CalendarEntity.CalendarEvent) {
        eventDao.insertEvent(event)
        firestore.collection("events").add(event).addOnSuccessListener {
            Log.d("Firestore","이벤트가 성공적으로 추가했습니다.")
        }.addOnFailureListener { e ->
            Log.w("Firestore", "추가가 실패되었습니다.", e)
        }
    }

    suspend fun getAll(date: String): List<CalendarEntity.CalendarEvent>{
        return eventDao.getAllEvents(date)
    }

    suspend fun update(event: CalendarEntity.CalendarEvent){
        eventDao.updateEvent(event)
        firestore.collection("events").document(event.id.toString()).update(
            mapOf(
                "title" to event.title,
                "description" to event.description,
                "date" to event.date,
                "time" to event.time
            )
        ).addOnSuccessListener {
            Log.d("Firestore","이벤트가 성공적으로 업데이트했습니다.")
        }.addOnFailureListener { e ->
                Log.w("Firestore", "업데이트가 실패되었습니다", e)
        }
    }

    suspend fun delete(event: CalendarEntity.CalendarEvent){
        eventDao.deleteEvent(event)
        firestore.collection("events").document(event.id.toString()).delete()
            .addOnSuccessListener {
                Log.d("Firestore", "이벤트가 성공적으로 삭제했습니다.")
            }.addOnFailureListener { e ->
                Log.w("Firestore", "삭제가 실패되었습니다.", e)
            }
    }
}