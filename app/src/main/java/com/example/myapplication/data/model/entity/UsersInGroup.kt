package com.example.myapplication.data.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UsersInGroup (
    @Embedded val group: GroupEntity,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "groupId"
    )
    val users : List<UserEntity>
)