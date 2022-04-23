package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.database.entity.UserEntity
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.mapper.toUserModel
import com.linc.inphoto.data.network.api.UserApiService
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.utils.extensions.isUrl
import com.linc.inphoto.utils.extensions.toMultipartBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService,
    private val authPreferences: AuthPreferences,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getLoggedInUser(): User? = withContext(ioDispatcher) {
        return@withContext getUserById(authPreferences.userId)
    }

    suspend fun getUserById(userId: String?): User? = withContext(ioDispatcher) {
        return@withContext userDao.getUserById(userId.orEmpty())?.toUserModel()
    }

    suspend fun updateUserAvatar(uri: Uri) = withContext(ioDispatcher) {
        val user: UserEntity?
        if (uri.isUrl()) {
            user = userApiService.updateUserAvatarUrl(
                authPreferences.userId,
                uri.toString()
            ).body?.toUserEntity()
        } else {
            val image = mediaLocalDataSource.createTempFile(uri) ?: return@withContext null
            user = userApiService.updateUserAvatar(
                authPreferences.userId,
                image.toMultipartBody()
            ).body?.toUserEntity()
            image.delete()
        }
        if (user != null) {
            userDao.updateUser(user)
        }
        return@withContext user?.toUserModel()
    }

    suspend fun updateUserHeader(uri: Uri) = withContext(ioDispatcher) {
        val user: UserEntity?
        if (uri.isUrl()) {
            user = userApiService.updateUserHeaderUrl(
                authPreferences.userId,
                uri.toString()
            ).body?.toUserEntity()
        } else {
            val image = mediaLocalDataSource.createTempFile(uri) ?: return@withContext null
            user = userApiService.updateUserHeader(
                authPreferences.userId,
                image.toMultipartBody()
            ).body?.toUserEntity()
            image.delete()
        }
        if (user != null) {
            userDao.updateUser(user)
        }
        return@withContext user?.toUserModel()
    }

    suspend fun updateUserName(name: String?) = withContext(ioDispatcher) {
        name ?: return@withContext
        userApiService.updateUserName(authPreferences.userId, name)
        userDao.getUserById(authPreferences.userId)?.let { user ->
            userDao.updateUser(user.copy(name = name))
        }
    }

    suspend fun updateUserStatus(status: String?) = withContext(ioDispatcher) {
        status ?: return@withContext
        userApiService.updateUserStatus(authPreferences.userId, status)
        userDao.getUserById(authPreferences.userId)?.let { user ->
            userDao.updateUser(user.copy(status = status))
        }
    }

    suspend fun updateUserGender(gender: Gender?) = withContext(ioDispatcher) {
        gender ?: return@withContext
        userApiService.updateUserGender(authPreferences.userId, gender.name)
        userDao.updateUserGender(authPreferences.userId, gender.name)
//        userDao.getUserById(authPreferences.userId)?.let { user ->
//            userDao.updateUserGender(user.copy(gender = gender))
//        }
    }

}