package com.example.myapplication.data.model.mapper

import com.example.myapplication.data.model.DTO.ProfileDTO
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.model.entity.ProfileEntity
import com.example.myapplication.data.model.entity.UserEntity

fun ProfileEntity.toDTO(): ProfileDTO {
    return ProfileDTO(
        userId = this.userId,
        nickname = this.nickname,
        name = this.name ,
        gender = this.gender,
        birthdate = this.birthdate

        )
}
//DTO를 Entity로 변환하는 확장 함수
fun ProfileDTO.toEntity(): ProfileEntity {
    return ProfileEntity(
        userId = this.userId,
        nickname = this.nickname,
        name = this.name ,
        gender = this.gender,
        birthdate = this.birthdate

        )
}