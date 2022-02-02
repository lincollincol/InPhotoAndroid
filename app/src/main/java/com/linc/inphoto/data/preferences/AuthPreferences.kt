package com.linc.inphoto.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class AuthPreferences @Inject constructor(
    preferences: SharedPreferences
) : BasePreferences(preferences) {

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "user_id"
    }

    var accessToken: String?
        get() = get(ACCESS_TOKEN)
        set(value) = put(ACCESS_TOKEN, value)

    var refreshToken: String?
        get() = get(REFRESH_TOKEN)
        set(value) = put(REFRESH_TOKEN, value)

    var userId: String?
        get() = get(USER_ID)
        set(value) = put(USER_ID, value)

    fun clear() {
        accessToken = null
        refreshToken = null
        userId = null
    }
}