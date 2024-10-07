package com.example.myapplication.data.repository

import com.example.myapplication.data.local.UserGroupDao
import com.example.myapplication.data.model.entity.GroupEntity
import com.example.myapplication.data.model.entity.UsersInGroup

class UserGroupRepository(private val userGroupDao: UserGroupDao){

    suspend fun isUserInGroup(userId : Long, groupId : Long): Boolean {
        return userGroupDao.isUserInGroup(userId,groupId) > 0
    }

    suspend fun addUserTOGroup(userId: Long, groupId: Long){
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