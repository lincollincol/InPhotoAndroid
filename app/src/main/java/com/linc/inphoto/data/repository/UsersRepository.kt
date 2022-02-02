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
        val userId = authPreferences.userId ?: return@withContext null
        return@withContext userDao.getUserById(userId).toUserModel()
    }

    suspend fun getUser(usedId: String) = withContext(ioDispatcher) {

    }

    suspend fun hasUserData(): Boolean = withContext(ioDispatcher) {
        val userId = authPreferences.userId ?: return@withContext false
        val user = userDao.getUserById(userId)
        return@withContext true
    }

}