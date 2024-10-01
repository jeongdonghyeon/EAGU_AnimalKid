package com.example.myapplication.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name ="userName")
    val userName : String,
    @ColumnInfo(name ="password")
    val password : String,
    @ColumnInfo(name ="email")
    val email: String,
    @ColumnInfo(name ="nickname")
    val nickname : String? = null,
    @ColumnInfo(name ="level")
    val level : Int = 0,
    @ColumnInfo(name ="exp")
    val exp: Double = 0.0,
    @ColumnInfo(name = "petName")
    val petName : String? = null
)