package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.mapper.toModel
import com.linc.inphoto.data.network.api.StoryApiService
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.entity.story.UserStory
import com.linc.inphoto.utils.extensions.toMultipartBody
import com.linc.inphoto.utils.extensions.toMultipartPart
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyApiService: StoryApiService,
    private val authPreferences: AuthPreferences,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun createUserStory(
        uri: Uri,
        expirationTimestamp: Long,
        durationMillis: Long,
    ) = withContext(ioDispatcher) {
        val image = mediaLocalDataSource.createTempFile(uri) ?: return@withContext null
        storyApiService.createUserStory(
            authPreferences.userId,
            image.toMultipartBody(),
            expirationTimestamp.toMultipartPart("expirationTimestamp"),
            durationMillis.toMultipartPart("durationMillis"),
        ).body
        image.delete()
    }

    suspend fun loadCurrentUserFollowingStories(): List<UserStory> = withContext(ioDispatcher) {
        val userStories = async { storyApiService.getUserStories(authPreferences.userId).body }
        val followingStories = async { loadUserFollowingStories(authPreferences.userId) }
        return@withContext listOfNotNull(
            userStories.await()?.let { it.toModel(isLoggedInUser(it.userId)) },
            *followingStories.await().toTypedArray()
        )
    }

    suspend fun loadCurrentUserStories(): UserStory? = withContext(ioDispatcher) {
        return@withContext loadUserStories(authPreferences.userId)
    }

    suspend fun loadUserStories(
        userId: String
    ): UserStory? = withContext(ioDispatcher) {
        return@withContext storyApiService.getUserStories(userId).body
            ?.let { it.toModel(isLoggedInUser(it.userId)) }
    }

    suspend fun loadUserFollowingStories(
        userId: String
    ): List<UserStory> = withContext(ioDispatcher) {
        return@withContext storyApiService.getUserFollowingStories(userId).body
            ?.map { it.toModel(isLoggedInUser(it.userId)) }
            .orEmpty()
    }

    suspend fun isLoggedInUser(userId: String?): Boolean = withContext(ioDispatcher) {
        return@withContext authPreferences.userId == userId
    }
}