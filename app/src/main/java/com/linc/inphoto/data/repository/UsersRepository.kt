package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.FileSystemManager
import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.mapper.toUserModel
import com.linc.inphoto.data.network.api.UserApiService
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApiService: UserApiService,
    private val authPreferences: AuthPreferences,
    private val fileSystemManager: FileSystemManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getLoggedInUser(): User? = withContext(ioDispatcher) {
        return@withContext getUserById(authPreferences.userId)
    }

    suspend fun getUserById(userId: String?): User? = withContext(ioDispatcher) {
        return@withContext userDao.getUserById(userId.orEmpty())?.toUserModel()
    }

    suspend fun updateUserAvatar(uri: Uri?) = withContext(ioDispatcher) {
        val image = fileSystemManager.uriToFile(uri) ?: return@withContext
        val requestBody = image.asRequestBody("multipart/form-data".toMediaType())
        val body = MultipartBody.Part.createFormData("image", image.name, requestBody)
        userApiService.updateUserAvatar(body, authPreferences.userId)
        image.delete()
    }

}