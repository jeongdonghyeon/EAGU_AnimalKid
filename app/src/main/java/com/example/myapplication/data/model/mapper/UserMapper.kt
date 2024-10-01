package com.example.myapplication.data.model.mapper

import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.model.entity.UserEntity

//Entity를 DTO로 변환하는 확장 함수
fun UserEntity.toDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        userName = this.userName,
        password = this.password,
        email = this.email,
        nickname = this.nickname,
        level = this.level,
        exp = this.exp,
        petName = this.petName

    )
}
//DTO를 Entity로 변환하는 확장 함수
fun UserDTO.toEntity(): UserEntity{
    return UserEntity(
        id = this.id,
        userName = this.userName,
        password = this.password,
        email = this.email,
        nickname = this.nickname,
        level = this.level,
        exp = this.exp,
        petName = this.petName
    )
}