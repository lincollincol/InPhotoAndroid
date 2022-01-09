package com.linc.inphoto.data.repository

import com.linc.inphoto.data.api.model.auth.SignInApiModel
import com.linc.inphoto.data.api.model.auth.SignUpApiModel
import com.linc.inphoto.data.api.model.user.UserApiModel
import com.linc.inphoto.data.api.service.AuthService
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.storage.LocalPreferences
import com.linc.inphoto.data.storage.database.dao.UserDao
import com.linc.inphoto.utils.Constants.ACCESS_TOKEN
import com.linc.inphoto.utils.Constants.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val localPreferences: LocalPreferences
) {

    suspend fun signIn(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        val response = authService.signIn(SignInApiModel(email, password))
        saveUser(response.body!!)
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        val response = authService.signUp(SignUpApiModel(email, username, password))
        saveUser(response.body!!)
    }

    private suspend fun saveUser(user: UserApiModel) {
        // Save base access user data
        localPreferences.put(ACCESS_TOKEN, user.accessToken)
        localPreferences.put(USER_ID, user.id)

        // Save user
        userDao.insertUser(user.toUserEntity())
    }

}