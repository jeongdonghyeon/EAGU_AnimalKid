package com.example.myapplication.data.model.entity.GroupCrossRef

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName ="user_group",
    primaryKeys = ["userId","groupId"],
    indices = [Index(value = ["userId","groupId"],unique = true)]
)
data class UserGroupCrossRef (
    val userId: Long,
    val groupId: Long,
)
