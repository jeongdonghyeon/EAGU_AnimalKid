package com.example.myapplication.data.model.DTO

data class ProfileDTO (
    val userId: String, // UserEntity와 관계를 맺기 위한 외래 키
    val nickname: String? = null,
    val name: String? = null,
    val gender: String? = null,
    val birthdate: String? = null
)
