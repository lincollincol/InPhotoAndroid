package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.database.dao.FollowerDao
import com.linc.inphoto.data.database.dao.UserDao
import com.linc.inphoto.data.database.entity.FollowerEntity
import com.linc.inphoto.data.database.entity.UserEntity
import com.linc.inphoto.data.mapper.toUserEntity
import com.linc.inphoto.data.mapper.toUserModel
import com.linc.inphoto.data.network.api.UserApiService
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.utils.extensions.EMPTY
import com.linc.inphoto.utils.extensions.isUrl
import com.linc.inphoto.utils.extensions.toMultipartBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val followerDao: FollowerDao,
    private val userApiService: UserApiService,
    private val authPreferences: AuthPreferences,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchUserById(userId: String) = withContext(ioDispatcher) {
        val user = async { userApiService.getUserById(userId).body?.toUserEntity() }
        val followingIds = async {
            userApiService.getUserFollowingIds(userId).body
                ?.map { FollowerEntity(it, authPreferences.userId) }
                .orEmpty()
        }
        user.await()?.let { userDao.upsertUser(it) }
        followerDao.run {
            deleteUserFollowing(userId)
            upsert(followingIds.await())
        }
    }

    suspend fun fetchLoggedInUser() = withContext(ioDispatcher) {
        fetchUserById(authPreferences.userId)
    }

    suspend fun getLoggedInUser(): User? = withContext(ioDispatcher) {
        return@withContext userDao.getUserById(authPreferences.userId)
            ?.toUserModel(isLoggedInUser = true, isFollowingUser = false)
    }

    suspend fun getUserById(userId: String?): User? = withContext(ioDispatcher) {
        return@withContext userApiService.getUserById(userId.toString()).body
            ?.toUserModel(isFollowingUser(userId), isLoggedInUser(userId))
    }

    suspend fun loadAllUsers(): List<User> = withContext(ioDispatcher) {
        return@withContext loadUsers(String.EMPTY)
    }

    suspend fun loadUsers(query: String): List<User> = withContext(ioDispatcher) {
        return@withContext userApiService.getUsers(query).body
            ?.map { it.toUserModel(isFollowingUser(it.id), isLoggedInUser(it.id)) }
            ?.filter { it.id != authPreferences.userId }
            .orEmpty()
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
        return@withContext user?.toUserModel(isLoggedInUser = true, isFollowingUser = false)
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
        return@withContext user?.toUserModel(isLoggedInUser = true, isFollowingUser = false)
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
    }

    suspend fun followUser(userId: String?): User? = withContext(ioDispatcher) {
        val user = userApiService.followUser(userId.toString(), authPreferences.userId).body
        val followerEntity = FollowerEntity(userId.toString(), authPreferences.userId)
        followerDao.insert(followerEntity)
        userDao.updateIncreaseUserFollowingCount(authPreferences.userId)
        return@withContext user?.toUserModel(isFollowingUser(userId), isLoggedInUser(userId))
    }

    suspend fun unfollowUser(userId: String?): User? = withContext(ioDispatcher) {
        val user = userApiService.unfollowUser(userId.toString(), authPreferences.userId).body
        val followerEntity = FollowerEntity(userId.toString(), authPreferences.userId)
        followerDao.delete(followerEntity)
        userDao.updateDecreaseUserFollowingCount(authPreferences.userId)
        return@withContext user?.toUserModel(isFollowingUser(userId), isLoggedInUser(userId))
    }

    suspend fun loadLoggedInUserFollowers(): List<User> = withContext(ioDispatcher) {
        return@withContext loadUserFollowers(authPreferences.userId)
    }

    suspend fun loadLoggedInUserFollowing(): List<User> = withContext(ioDispatcher) {
        return@withContext loadUserFollowing(authPreferences.userId)
    }

    suspend fun loadUserFollowers(userId: String?): List<User> = withContext(ioDispatcher) {
        return@withContext userApiService.getUserFollowers(userId.toString()).body
            ?.map { it.toUserModel(isFollowingUser(userId), isLoggedInUser(userId)) }
            .orEmpty()
    }

    suspend fun loadUserFollowing(userId: String?): List<User> = withContext(ioDispatcher) {
        return@withContext userApiService.getUserFollowing(userId.toString()).body
            ?.map { it.toUserModel(isFollowingUser(userId), isLoggedInUser(userId)) }
            .orEmpty()
    }

    suspend fun isFollowingUser(userId: String?): Boolean = withContext(ioDispatcher) {
        try {
            val follower = followerDao.loadFollower(
                userId.toString(),
                authPreferences.userId
            )
            return@withContext follower != null
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }

    suspend fun isLoggedInUser(userId: String?): Boolean = withContext(ioDispatcher) {
        return@withContext authPreferences.userId == userId
    }
}