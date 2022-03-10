package com.linc.inphoto.data.android

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.utils.extensions.getUriExtension
import com.linc.inphoto.utils.extensions.readUriBytes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
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

    suspend fun getFileFromUri(uri: Uri?): File? = withContext(ioDispatcher) {
        uri ?: return@withContext null
        val contentResolver = context.contentResolver
        val fileName = UUID.randomUUID().toString()
        val data = contentResolver.readUriBytes(uri) ?: return@withContext null
        val extension = contentResolver.getUriExtension(uri)
        return@withContext File(context.cacheDir.path, "$fileName.$extension")
            .also { audio -> audio.writeBytes(data) }
    }

    fun createTempUri(): Uri {
        return createTempFile().toUri()
    }

    fun createTempFile(): File {
        val temp = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DCIM),
            UUID.randomUUID().toString()
        )
        temp.createNewFile()
        temp.deleteOnExit()
        return temp
    }

    fun createTempFromBmp(bitmap: Bitmap): File {
        val file = createTempFile()
        val os = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        os.close()
        return file
    }

    fun copyUri(src: Uri, dst: Uri) {
        val inputStream = context.contentResolver.openInputStream(src)
        val outputStream = context.contentResolver.openOutputStream(dst)
        val b = ByteArray(4096)
        var read: Int = -1
        while (inputStream?.read(b)?.also { read = it } != -1) {
            outputStream?.write(b, 0, read)
        }
        outputStream?.flush()
        outputStream?.close()
        inputStream.close()
    }
}