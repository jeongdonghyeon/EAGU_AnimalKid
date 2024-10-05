package com.example.myapplication.data.repository

import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.model.entity.UserEntity
import com.example.myapplication.data.model.mapper.toEntity

class UserRepository(private val userDao: UserDao) {
    // viewModel 에 받아온 유저 DTO를 유저 엔티티로 변환하고 UserDao 에 전달
    suspend fun registerUser(userDTO: UserDTO) {
        val userEntity = userDTO.toEntity();
        userDao.insertUser(userEntity);
    }
    // viewModel 에 받아온 구글 유저 DTO를 유저 엔티티로 변환하고 UserDao 에 전달
    suspend fun registerGoogleUser(userDTO: UserDTO){
        val userEntity = userDTO.toEntity()
        userDao.insertUser(userEntity)
    }
    //viewModel 에 빋아온 유저 DTO를 유저 엔티티로 변환하고 UserDao 에 전달
    suspend fun unregisterUser(userDTO: UserDTO){
        val userEntity = userDTO.toEntity();
        userDao.deleteUser(userEntity);
    }
    //viewModel 에 받아온 유저 DTO 중 email, nickname, petName 만 엔티티로 변환하고 UserDao 에 전달
    //만약 nickname 과 petName 둘다 null 인경우 viewModel 에 false 를 전달하고 아니라면 true 를 전달한다.
    suspend fun saveProfile(userDTO: UserDTO):Boolean{
        val email = userDTO.email;
        val nickname = userDTO.nickname;
        val petName = userDTO.petName;
        if(nickname.isNullOrEmpty() || petName.isNullOrEmpty()){
            return false;
        }
        userDao.updateUserProfile(email,nickname,petName);

        return true;
    }
    //viewModel 에 받아온 email 을 UserDao 에 전달
    suspend fun  findUserName(userDTO: UserDTO){
        val email = userDTO.email;
        userDao.findUsername(email);
    }//viewModel 에 받아온 newPassword 와 email 을 UserDao 에 전달
    suspend fun updatePassword(userDTO: UserDTO){
        val newPassword =userDTO.password;
        val email = userDTO.email;
        userDao.updatePassword(email,newPassword);
    }

    suspend fun getUserByUsername(username: String):UserEntity?{
        return userDao.getUserByUsername(username)
    }

}