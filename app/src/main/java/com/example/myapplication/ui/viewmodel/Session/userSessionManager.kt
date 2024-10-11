package com.example.myapplication.ui.viewmodel.Session

import android.content.Context

object UserSessionManager {
    private const val PREFS_NAME = "user_prefs"
    private const val USER_ID_KEY = "userId"

    // UUID를 SharedPreferences에서 가져오는 함수
    fun getUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID_KEY, null) // 없으면 null 반환
    }

    // UUID를 SharedPreferences에 저장하는 함수
    fun saveUserId(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY, userId)
            apply()
        }
    }
    fun clearUserSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove(USER_ID_KEY) // 저장된 userId를 삭제
            apply()
        }
    }
}
