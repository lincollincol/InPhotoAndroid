package com.linc.inphoto.data.android

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.utils.extensions.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.InputStream
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
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
            ),
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idIndex = it.getColumnIndex(MediaStore.Images.Media._ID)
                    val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val dateIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val dataIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    val name = it.getString(nameIndex)
                    val date = it.getLong(dateIndex)
                    val data = it.getString(dataIndex)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it.getLong(idIndex)
                    )

                    val mediaExist = when {
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> File(data).exists()
                        else -> DocumentFile.fromSingleUri(context, contentUri)?.exists() ?: false
                    }

                    if (mediaExist) {
                        content.add(LocalMedia(name, date, contentUri))
                    }
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

    fun checkRight(uri: Uri?): Boolean {
        if (uri == null) return false
        val resolver = context.contentResolver
        //1. Check Uri
        var cursor: Cursor? = null
        val isUriExist: Boolean = try {
            cursor = resolver.query(uri, null, null, null, null)
            //cursor null: content Uri was invalid or some other error occurred
            //cursor.moveToFirst() false: Uri was ok but no entry found.
            (cursor != null && cursor.moveToFirst())
        } catch (t: Throwable) {
            Timber.e("1.Check Uri Error: ${t.message}")
            false
        } finally {
            try {
                cursor?.close()
            } catch (t: Throwable) {
            }
        }
        //2. Check File Exist
        // If the system db has Uri related records, but the file is invalid or damaged
        var ins: InputStream? = null
        val isFileExist: Boolean = try {
            ins = resolver.openInputStream(uri)
            // file exists
            true
        } catch (t: Throwable) {
            // File was not found eg: open failed: ENOENT (No such file or directory)
            Timber.e("2. Check File Exist Error: ${t.message}")
            false
        } finally {
            try {
                ins?.close()
            } catch (t: Throwable) {
            }
        }
        return isUriExist && isFileExist
    }
}