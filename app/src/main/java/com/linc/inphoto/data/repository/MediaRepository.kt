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

    /**
     * Create temporary copy of `uri` and delete source `uri`
     */
    fun convertToTempUri(uri: Uri): Uri {
        val tempUriCopy = createTempUri(uri)
        deleteLocalUri(uri)
        return tempUriCopy
    }

    /**
     * Create temporary copy of `uri`
     */
    fun createTempUri(uri: Uri): Uri {
        return mediaLocalDataSource.copyToTempUri(uri)
    }

    /**
     * Create temporary copy of `bitmap`
     */
    fun createTempUri(bitmap: Bitmap): Uri {
        val imageFile = mediaLocalDataSource.createTempFile(bitmap)
        return Uri.fromFile(imageFile)
    }

    fun deleteLocalUri(uri: Uri) {
        mediaLocalDataSource.deleteUri(uri)
    }

}