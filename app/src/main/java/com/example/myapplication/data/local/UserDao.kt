package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.model.entity.UserEntity

@Dao
interface UserDao {

    // UserRepository 에서 받아온 유저 엔티티를 데이터 스키마에 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // 데이터 스키마에서 UserRepository 에게 받아온 유저 엔티티와 동일한 행을 삭제
    @Delete
    suspend fun deleteUser(user: UserEntity)

    //UserRepository 에서 받아온 이메일, 닉네임, 펫네임을 가지고 이메일을 기반으로 닉네임, 펫네임을 업데이트함
    @Query("UPDATE users SET nickname = :nickname, petName = :petName WHERE email = :email")
    suspend fun updateUserProfile(email: String, nickname: String?, petName: String?): Int

    // UserRepository 에서 받아온 email 을 기반으로 userName 을 받아옴
    @Query("SELECT userName FROM users where email = :email")
    suspend fun findUsername( email: String): UserEntity?

    // UserRepository 에서 받아온 이메일과 새로운 비밀번호를 가지고 이메일 기반으로 유저의 비밀번호를 업데이트함
    @Query("UPDATE users SET password = :newPassword where email =:email")
    suspend fun updatePassword(email: String, newPassword: String?)

    @Query("SELECT * From users WHERE userName = :userName LIMIT 1")
    suspend fun  getUserByUsername(userName: String):UserEntity?



}