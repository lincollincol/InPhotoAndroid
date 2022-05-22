package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.network.api.StoryApiService
import com.linc.inphoto.data.preferences.AuthPreferences
import com.linc.inphoto.utils.extensions.toMultipartBody
import com.linc.inphoto.utils.extensions.toMultipartPart
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

}