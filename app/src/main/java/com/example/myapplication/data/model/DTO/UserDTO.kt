package com.example.myapplication.data.model.DTO

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.UUID

data class  UserDTO(
    val userId: String = UUID.randomUUID().toString(),
    val userName: String? = null,
    val password: String? = null,
    val email: String,
    val level: Int = 0,
    val exp: Double = 0.0,
    val petName: String? = null,
    val groupId: Long? = null

)