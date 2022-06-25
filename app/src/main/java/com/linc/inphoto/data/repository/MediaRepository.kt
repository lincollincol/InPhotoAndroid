package com.linc.inphoto.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.linc.inphoto.data.android.MediaLocalDataSource
import com.linc.inphoto.data.network.api.ContentApiService
import com.linc.inphoto.entity.media.image.ImageSticker
import com.linc.inphoto.entity.user.Gender
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val contentApiService: ContentApiService,
    private val mediaLocalDataSource: MediaLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadGalleryImages() = withContext(ioDispatcher) {
        return@withContext mediaLocalDataSource.loadImageFiles()
    }

    suspend fun loadLocalFiles(mimeType: String) = withContext(ioDispatcher) {
        return@withContext mediaLocalDataSource.loadLocalFiles(mimeType)
    }

    suspend fun loadAudioFiles() = withContext(ioDispatcher) {
        return@withContext mediaLocalDataSource.loadAudioFiles()
    }

    suspend fun loadRandomUserAvatar(gender: Gender?) = withContext(ioDispatcher) {
        return@withContext contentApiService.getRandomAvatar(gender?.name).body
    }

    suspend fun loadRandomUserHeader() = withContext(ioDispatcher) {
        return@withContext contentApiService.getRandomHeader().body
    }

    suspend fun loadStickers() = withContext(ioDispatcher) {
        return@withContext contentApiService.getStickers().body
            ?.map { ImageSticker(UUID.randomUUID().toString(), Uri.parse(it)) }
            .orEmpty()
    }

    /**
     * Create temporary copy of `uri` and delete source `uri`
     */
    fun convertToTempUri(uri: Uri): Uri {
        val tempUriCopy = createTempUri(uri)
        deleteLocalUri(uri)
        return tempUriCopy
    }

    fun getMediaFromUri(uri: Uri) = mediaLocalDataSource.getMediaFromUri(uri)

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