package com.example.myapplication.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users",
    indices = [Index(value =["email"],unique = true),
        Index(value =["userName"],unique = true)])
data class UserEntity(
    @PrimaryKey
    val userId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name ="userName")
    val userName : String? = null,
    @ColumnInfo(name ="password")
    val password : String? = null,
    @ColumnInfo(name ="email")
    val email: String,
    @ColumnInfo(name ="nickname")
    val nickname : String? = null,
    @ColumnInfo(name ="level")
    val level : Int = 0,
    @ColumnInfo(name ="exp")
    val exp: Double = 0.0,
    @ColumnInfo(name = "petName")
    val petName : String? = null,
    @ColumnInfo(name = "groupId")
    val groupId: Long? = null,

    @ColumnInfo(name = "name")
    val name:String?=null,   //이름
    @ColumnInfo(name = "gender")
    val gender: String?=null,    //성별
    @ColumnInfo(name = "birthdate")
    val birthdate: String?=null  //생년월일
)