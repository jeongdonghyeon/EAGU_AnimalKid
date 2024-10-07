package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DTO.GroupDTO
import com.example.myapplication.data.model.DTO.UserDTO
import com.example.myapplication.data.repository.UserGroupRepository

import kotlinx.coroutines.launch

class groupViewModel(private val userGroupRepository: UserGroupRepository) : ViewModel() {


    private val _invitationResult = MutableLiveData<String>()
    val invitationResult: LiveData<String> get() = _invitationResult
    fun acceptInvitation(userDTO: UserDTO,groupDTO: GroupDTO){
        viewModelScope.launch {
            try{
                userGroupRepository.addUserTOGroup(userDTO.userId,groupDTO.groupId)
                _invitationResult.postValue(" 그룹에 성공적으로 들어왔습니다.")
            } catch(e : Exception){
                _invitationResult.postValue(" 오류: 그룹에 들어오지 못했습니다, 다시 시도하십시오")
            }
        }
    }

    fun declineInvitation(){
        viewModelScope.launch {
            try{
                _invitationResult.postValue("그룹 초대를 거절했습니다.")
            } catch(e : Exception){
                _invitationResult.postValue("오류: 그룹 초대 거절하는 것을 실패하셨습니다.")
            }
        }
    }

}