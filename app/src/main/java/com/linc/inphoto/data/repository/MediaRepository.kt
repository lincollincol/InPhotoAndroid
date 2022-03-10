package com.linc.inphoto.data.repository

import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadGalleryImages() = withContext(ioDispatcher) {
        mediaLocalDataSource.loadDCIMFiles()
    }

    fun copyToTempUri(src: Uri): Uri {
        val imageUri = mediaLocalDataSource.createTempUri()
        mediaLocalDataSource.copyUri(src, imageUri)
        return imageUri
    }

    fun getTempOutputUri(): Uri {
        return mediaLocalDataSource.createTempUri()
    }
}