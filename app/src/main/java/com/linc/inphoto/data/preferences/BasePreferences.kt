package com.linc.inphoto.data.preferences

import android.content.SharedPreferences

abstract class BasePreferences constructor(
    protected val preferences: SharedPreferences
) {

    companion object {
        const val PREFERENCES_NAME = "InPhotoPreferences"
    }

    protected fun <T> put(key: String, value: T) {
        val editor = preferences.edit()

        val task = when (value) {
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
    protected fun <T> get(key: String): T? {
        return preferences.all[key] as? T
    }

    protected fun has(key: String): Boolean {
        return preferences.contains(key)
    }

}