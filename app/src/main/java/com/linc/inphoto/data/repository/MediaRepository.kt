package com.linc.inphoto.data.repository

import android.graphics.Bitmap
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

    fun createTempUri(bitmap: Bitmap): Uri {
        val imageFile = mediaLocalDataSource.createTempFromBmp(bitmap)
        return Uri.fromFile(imageFile)
    }

}