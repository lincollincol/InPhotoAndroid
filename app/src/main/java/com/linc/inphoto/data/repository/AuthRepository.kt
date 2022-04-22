package com.linc.inphoto.data.repository

import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.network.api.AuthApiService
import com.linc.inphoto.data.network.model.auth.SignInApiModel
import com.linc.inphoto.data.network.model.auth.SignUpApiModel
import com.linc.inphoto.data.network.model.user.UserApiModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.user.Gender
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
        if (response.failed) {
            throw Exception(response.status)
        }
        saveUser(response.body!!)
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
        gender: Gender,
    ) = withContext(ioDispatcher) {
        // TODO: 13.03.22 check sign up response for access token
        val response = authApiService.signUp(SignUpApiModel(email, username, password, gender))
        if (response.failed) {
            throw Exception(response.status)
        }
        saveUser(response.body!!)
    }

    suspend fun signOut() = withContext(ioDispatcher) {
        authPreferences.clear()
        userDao.deleteUsers()
    }

    private suspend fun saveUser(user: UserApiModel) = withContext(ioDispatcher) {
        // Save base access user data
        with(authPreferences) {
            userId = user.id
            accessToken = user.accessToken
            // TODO: 02.02.22 replace with refreshToken
            refreshToken = user.accessToken
        }

        // Save user
        userDao.insertUser(user.toUserEntity())
    }

}