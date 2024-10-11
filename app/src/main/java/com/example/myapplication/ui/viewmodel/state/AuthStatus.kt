package com.example.myapplication.ui.viewmodel.state

enum class  AuthAction {
    LOGIN, LOGOUT, REGISTER, UNREGISTER,FINDUSERNAME
}
sealed class AuthStatus(val action: AuthAction){
    class Loading(action: AuthAction) : AuthStatus(action)
    class Success(action: AuthAction) : AuthStatus(action)
    class Failure(action: AuthAction, val message: String) : AuthStatus(action)

}