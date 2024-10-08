package com.example.myapplication.data.repository

import android.app.Application
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.UserGroupDao
import com.example.myapplication.data.model.entity.GroupEntity
import com.example.myapplication.data.model.entity.UsersInGroup

class UserGroupRepository(application: Application){
    private val userGroupDao = AppDatabase.getDatabase(application).groupDao()
    suspend fun isUserInGroup(userId : Long, groupId : Long): Boolean {
        return userGroupDao.isUserInGroup(userId,groupId) > 0
    }

    suspend fun addUserTOGroup(userId: String, groupId: Long){
        userGroupDao.updateUserGroup(userId,groupId)
    }
    suspend fun deleteGroup(groupEntity: GroupEntity){
        userGroupDao.deleteGroup(groupEntity)
    }
    suspend fun insertGroup(groupEntity: GroupEntity){
        userGroupDao.insertGroup(groupEntity)
    }
    suspend fun getGroupWithUsers(groupId: Long): UsersInGroup? {
        return userGroupDao.getGroupWithUsers(groupId)
    }
}