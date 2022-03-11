package com.linc.inphoto.data.android

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.utils.extensions.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun loadDCIMFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        val content = mutableListOf<LocalMedia>()
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DISPLAY_NAME
            ),
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )
        Timber.d("Loaded count: ${cursor?.count}")
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
                    val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val dateIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val name = it.getString(nameIndex)
                    val date = it.getLong(dateIndex)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it.getLong(idIndex)
                    )
                    content.add(LocalMedia(name, date, contentUri))
                } while (it.moveToNext())
            }
        }
        return@withContext content
    }

    suspend fun createTempFile(uri: Uri?): File? = withContext(ioDispatcher) {
        uri ?: return@withContext null
        return@withContext context.createTempFile(uri)
    }

    fun createTempUri(): Uri = context.createTempUri()

    fun createTempFile(): File = context.createTempFile()

    fun createTempFile(bitmap: Bitmap): File = context.createTempFile(bitmap)

    fun copyToTempUri(src: Uri): Uri {
        val result = createTempUri()
        context.copyUri(src, result)
        return result
    }

    fun deleteUri(uri: Uri?) = context.deleteUri(uri)
}