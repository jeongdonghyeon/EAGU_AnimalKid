package com.example.myapplication.data.model.mapper

import com.example.myapplication.data.model.DTO.GroupDTO
import com.example.myapplication.data.model.entity.GroupEntity

fun GroupEntity.toDTO(): GroupDTO{
    return GroupDTO(
        id = this.id,
        groupName = this.groupName

    )
}
fun GroupDTO.toEntity(): GroupEntity{
    return GroupEntity(
        id = this.id,
        groupName = this.groupName

    )
}