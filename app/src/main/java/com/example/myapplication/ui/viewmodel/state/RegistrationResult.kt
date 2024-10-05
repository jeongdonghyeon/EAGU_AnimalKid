package com.example.myapplication.ui.viewmodel.state

sealed class RegistrationResult {
    object Success : RegistrationResult()
    data class Failure(val errorMessage: String) : RegistrationResult()
}