package com.linc.inphoto.data.storage

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

class LocalPreferences @Inject constructor(
    private val preferences: SharedPreferences
) {

    companion object {
        const val PREFERENCES_NAME = "InPhotoPreferences"
    }

    fun <T> put(key: String, value: T) {
        val editor = preferences.edit()

        val task = when(value) {
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            else -> null
        }

        task?.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String) : T {
        return preferences.all[key] as T
    }

    fun has(key: String): Boolean {
        return preferences.contains(key)
    }

}