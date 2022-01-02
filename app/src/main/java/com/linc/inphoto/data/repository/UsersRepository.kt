package com.linc.inphoto.data.repository

import com.linc.inphoto.data.mapper.toUserModel
import com.linc.inphoto.data.storage.LocalPreferences
import com.linc.inphoto.data.storage.database.dao.UserDao
import com.linc.inphoto.ui.common.model.user.UserModel
import com.linc.inphoto.utils.Constants.ACCESS_TOKEN
import com.linc.inphoto.utils.Constants.USER_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val userDao: UserDao,
    private val localPreferences: LocalPreferences,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getLoggedInUser() : UserModel {
        val userId = localPreferences.get<String>(USER_ID)
        return userDao.getUserById(userId).toUserModel()
    }

    suspend fun getUser(usedId: String) {

    }

    suspend fun hasUserData() : Boolean {
        localPreferences.get<String?>(ACCESS_TOKEN) ?: return false
        val userId = localPreferences.get<String?>(USER_ID) ?: return false
        val user = userDao.getUserById(userId) ?: return false
        return true
    }

}