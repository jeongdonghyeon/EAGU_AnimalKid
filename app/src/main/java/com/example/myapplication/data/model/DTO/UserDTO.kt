package com.example.myapplication.data.model.DTO

import java.util.UUID

data class  UserDTO(
    val userId: String = UUID.randomUUID().toString(),
    val userName: String? = null,
    val password: String? = null,
    val email: String,
    val nickname: String? = null,
    val level: Int = 0,
    val exp: Double = 0.0,
    val petName: String? = null,
    val groupId: Long? = null,
    val name: String?=null,
    val gender:String? =null,
    val birthdate: String?=null

)