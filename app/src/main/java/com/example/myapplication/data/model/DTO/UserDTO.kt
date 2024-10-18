package com.example.myapplication.data.model.DTO

import java.util.UUID

data class  UserDTO(
    val userId: String = UUID.randomUUID().toString(),
    val userName: String,
    val password: String?,
    val email: String,
    val nickname: String? = null,
    val level: Int = 0,
    val exp: Double = 0.0,
    val petName: String? = null,
    val groupId: Long? = null
)