package com.example.myapplication.data.model.DTO

data class UserDTO (
    val id: Long = 0L,
    val userName : String,
    val password : String,
    val email: String,
    val nickname : String?,
    val level : Int = 0,
    val exp: Double = 0.0,
    val petName : String?
)