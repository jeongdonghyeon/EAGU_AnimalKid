package com.example.myapplication.data.local

import android.adservices.adid.AdId
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.model.entity.GroupCrossRef.UserGroupCrossRef
import com.example.myapplication.data.model.entity.UserEntity

@Dao
interface UserGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInGroup(userGroup:UserGroupCrossRef)
    @Query("SELECT * FROM users WHERE groupId =:groupId")
    suspend fun getUsersByGroupId(groupId: Long): List<UserEntity>
}