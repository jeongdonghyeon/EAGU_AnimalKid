package com.example.myapplication.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "profiles",
    indices = [Index(value = ["userId"], unique = true)] // 외래 키 대신 인덱스만 유지
)
data class ProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId: String, // 단순 참조 필드
    @ColumnInfo(name = "nickname")
    val nickname: String? = null,
    @ColumnInfo(name = "name")
    val name: String? = null,
    @ColumnInfo(name = "gender")
    val gender: String? = null,
    @ColumnInfo(name = "birthdate")
    val birthdate: String? = null
)
