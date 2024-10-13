package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.model.entity.GroupEntity
import com.example.myapplication.data.model.entity.UserEntity

@Dao
interface UserDao {

    // UserRepository 에서 받아온 유저 엔티티를 데이터 베이스에 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // 데이터 스키마에서 UserRepository 에게 받아온 유저 엔티티와 동일한 행을 삭제
    @Delete
    suspend fun deleteUser(user: UserEntity)

    //UserRepository 에서 받아온 이메일, 닉네임, 펫네임을 가지고 이메일을 기반으로 닉네임, 펫네임을 업데이트함
    @Query("UPDATE users SET nickname = :nickname, petName = :petName WHERE email = :email")
    suspend fun updateUserProfile(email: String, nickname: String?, petName: String?): Int
/*
    // UserRepository 에서 받아온 email 을 기반으로 userName 을 받아옴
    @Query("SELECT userName FROM users where email = :email")
    suspend fun findUsername( email: String): UserEntity?
*/
    // UserRepository 에서 받아온 이메일과 새로운 비밀번호를 가지고 이메일 기반으로 유저의 비밀번호를 업데이트함
    @Query("UPDATE users SET password = :newPassword where email =:email")
    suspend fun updatePassword(email: String, newPassword: String?)
/*
    // 사용자의 email을 기반으로 level 를 가져옴
    @Query("SELECT level From users WHERE email = :email ")
    suspend fun getLevelByEmail(email: String)
*/
    // 사용자의 업데이트한 level을 데이터베이스에 적용시킴
    @Query("UPDATE users SET level = :level WHERE email = :email")
    suspend fun  setLevelByEmail(email: String, level: String)

/*
    // 사용자의 email을 기반으로 exp 를 가져옴
    @Query("SELECT exp From users WHERE email = :email")
    suspend fun  getExpByEmail(email: String)
*/
    // 사용자의 업데이트한 level을 데이터베이스에 적용시킴
    @Query("UPDATE users SET exp = :exp WHERE email = :email")
    suspend fun  setExpByEmail(email: String, exp: String)

    // 유저 네임으로 유저 가져오기
    @Query("SELECT * FROM users WHERE userName = :userName LIMIT 1")
    suspend fun getUserByUsername(userName: String):UserEntity?

    // 유저 id로 유저 가져오기
    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun getUserByUserId(userId: String?):UserEntity?












}