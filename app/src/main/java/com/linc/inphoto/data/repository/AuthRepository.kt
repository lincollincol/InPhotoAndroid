package com.linc.inphoto.data.repository

import com.linc.inphoto.data.api.dto.auth.SignInRequest
import com.linc.inphoto.data.api.dto.auth.SignUpRequest
import com.linc.inphoto.data.api.dto.user.UserResponse
import com.linc.inphoto.data.api.service.AuthService
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.storage.LocalPreferences
import com.linc.inphoto.data.storage.database.dao.UserDao
import com.linc.inphoto.utils.Constants.ACCESS_TOKEN
import com.linc.inphoto.utils.Constants.USER_ID
import com.linc.inphoto.utils.toException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val localPreferences: LocalPreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun signIn(
        email: String,
        password: String
    ) : Result<Unit> = withContext(dispatcher) {
        val response = authService.signIn(SignInRequest(email, password))
        if(response.success) {
            saveUser(response.body!!)
            return@withContext Result.success(Unit)
        }
        return@withContext Result.failure(response.error.toException())
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ) : Result<Unit> = withContext(dispatcher) {
        val response = authService.signUp(SignUpRequest(email, username, password))
        if(response.success) {
            saveUser(response.body!!)
            return@withContext Result.success(Unit)
        }
        return@withContext Result.failure(response.error.toException())
    }

    private suspend fun saveUser(user: UserResponse) {
        // Save base access user data
        localPreferences.put(ACCESS_TOKEN, user.accessToken)
        localPreferences.put(USER_ID, user.id)

        // Save user
        userDao.insertUser(user.toUserEntity())
    }

}