package com.example.myapplication.data.local

import android.adservices.adid.AdId
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.model.entity.GroupEntity
import com.example.myapplication.data.model.entity.UserEntity
import com.example.myapplication.data.model.entity.UsersInGroup

@Dao
interface UserGroupDao {
    // 그룹 ID로 그룹 가져오기
    @Transaction
    @Query("SELECT * FROM groups WHERE groupId = :groupId")
    suspend fun getGroupWithUsers(groupId: Long): UsersInGroup?
    // 그룹 생성
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)
    // 그룹 삭제
    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    // 유저가 그룹에 속해 있는지 확인
    @Query("SELECT COUNT(*) FROM users WHERE userId = :userId AND groupId = :groupId")
    suspend fun isUserInGroup(userId: Long, groupId: Long): Int

    // 유저의 그룹 업데이트 (그룹에 추가)
    @Query("UPDATE users SET groupId = :groupId WHERE userId = :userId")
    suspend fun  updateUserGroup(userId: String, groupId: Long)

}