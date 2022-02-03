package com.linc.inphoto.data.repository

import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.mapper.toUserModel
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val userDao: UserDao,
    private val authPreferences: AuthPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getLoggedInUser(): User? = withContext(ioDispatcher) {
        return@withContext getUserById(authPreferences.userId)
    }

    suspend fun getUserById(userId: String?): User? = withContext(ioDispatcher) {
        return@withContext userDao.getUserById(userId.orEmpty())?.toUserModel()
    }

}