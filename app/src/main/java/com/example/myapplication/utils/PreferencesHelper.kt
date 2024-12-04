package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "UserPreferences"

    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val KEY_PROFILE_SETUP_COMPLETE = "isProfileSetupComplete"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, value: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
    }

    fun isProfileSetupComplete(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_PROFILE_SETUP_COMPLETE, false)
    }

    fun setProfileSetupComplete(context: Context, value: Boolean) {
        getPreferences(context).edit().putBoolean(KEY_PROFILE_SETUP_COMPLETE, value).apply()
    }
}
