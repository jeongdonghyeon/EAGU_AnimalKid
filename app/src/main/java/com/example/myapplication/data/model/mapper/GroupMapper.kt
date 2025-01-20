package com.example.myapplication.data.model.mapper

import com.example.myapplication.data.model.DTO.GroupDTO
import com.example.myapplication.data.model.entity.GroupEntity

fun GroupEntity.toDTO(): GroupDTO{
    return GroupDTO(
        groupId = this.groupId,
        groupName = this.groupName

    )
}
fun GroupDTO.toEntity(): GroupEntity{
    return GroupEntity(
        groupId = this.groupId,
        groupName = this.groupName

    )
}