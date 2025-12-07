package com.example.animalcrossing.utils

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun saveUser(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getUser(): String? {
        return prefs.getString("email", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}