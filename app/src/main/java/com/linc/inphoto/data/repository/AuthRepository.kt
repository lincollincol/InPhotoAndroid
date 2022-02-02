package com.linc.inphoto.data.repository

import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.network.api.AuthApiService
import com.linc.inphoto.data.network.model.auth.SignInApiModel
import com.linc.inphoto.data.network.model.auth.SignUpApiModel
import com.linc.inphoto.data.network.model.user.UserApiModel
import com.linc.inphoto.data.preferences.AuthPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun signIn(
        email: String,
        password: String
    ) = withContext(ioDispatcher) {
        val response = authApiService.signIn(SignInApiModel(email, password))
        saveUser(response.body!!)
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ) = withContext(ioDispatcher) {
        val response = authApiService.signUp(SignUpApiModel(email, username, password))
        saveUser(response.body!!)
    }

    private suspend fun saveUser(user: UserApiModel) = withContext(ioDispatcher) {
        // Save base access user data
        with(authPreferences) {
            accessToken = user.accessToken
            // TODO: 02.02.22 replace with refreshToken
            refreshToken = user.accessToken
        }

        // Save user
        userDao.insertUser(user.toUserEntity())
    }

}