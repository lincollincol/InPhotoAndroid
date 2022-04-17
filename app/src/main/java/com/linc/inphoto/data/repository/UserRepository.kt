package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.mapper.toUserModel
import com.linc.inphoto.data.network.api.UserApiService
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.User
import com.rhythmoya.data.network.helper.HttpHelper.MediaType.MULTIPART_FORM_DATA
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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

    suspend fun updateUserAvatar(uri: Uri?): User? = withContext(ioDispatcher) {
        val image = mediaLocalDataSource.createTempFile(uri) ?: return@withContext null
        val requestBody = image.asRequestBody(MULTIPART_FORM_DATA.toMediaType())
        val body = MultipartBody.Part.createFormData(image.name, image.name, requestBody)
        val response = userApiService.updateUserAvatar(body, authPreferences.userId)
        val user = response.body?.toUserEntity()
        if (user != null) {
            userDao.updateUser(user)
        }
        image.delete()
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

}