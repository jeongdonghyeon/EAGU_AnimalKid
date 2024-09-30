package com.example.myapplication.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val email: String,
    val nickname : String?,
    val level : Int = 0,
    val exp: Double = 0.0
)