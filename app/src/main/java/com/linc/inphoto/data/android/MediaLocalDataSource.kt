package com.linc.inphoto.data.android

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.utils.extensions.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import linc.com.amplituda.Amplituda
import timber.log.Timber
import java.io.File
import java.io.InputStream
import javax.inject.Inject


class MediaLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val amplituda: Amplituda,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun loadLocalFiles(mimeType: String): List<LocalMedia> = withContext(ioDispatcher) {
        return@withContext when {
            mimeType.isAudioMimeType() -> loadAudioFiles()
            mimeType.isImageMimeType() -> loadImageFiles()
            mimeType.isVideoMimeType() -> loadVideoFiles()
            mimeType.isDocMimeType() -> loadDocumentsFiles()
            else -> emptyList()
        }
    }

    suspend fun loadImageFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        return@withContext loadExternalFiles(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    suspend fun loadVideoFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        return@withContext loadExternalFiles(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    }

    suspend fun loadAudioFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        return@withContext loadExternalFiles(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
    }

    suspend fun loadDocumentsFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        val files = when {
            SDK_INT >= Build.VERSION_CODES.Q -> loadDownloadsFiles()
            else -> loadExternalFiles(MediaStore.Files.getContentUri("external"))
        }
        return@withContext files.filter { it.mimeType.isDocMimeType() }
    }

    private suspend fun loadExternalFiles(
        externalContentUri: Uri
    ): List<LocalMedia> = withContext(ioDispatcher) {
        val content = mutableListOf<LocalMedia>()
        val cursor = context.contentResolver.query(
            externalContentUri,
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.DATA,
            ),
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idIndex = it.getColumnIndex(MediaStore.MediaColumns._ID)
                    val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    val titleIndex = it.getColumnIndex(MediaStore.MediaColumns.TITLE)
                    val mimeTypeIndex = it.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)
                    val dateIndex = it.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)
                    val dataIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA)
                    val name = it.getString(nameIndex)
                    val title = it.getString(titleIndex)
                    val mimeType = it.getString(mimeTypeIndex)
                    val date = it.getLong(dateIndex)
                    val data = it.getString(dataIndex)
                    val contentUri = ContentUris.withAppendedId(
                        externalContentUri,
                        it.getLong(idIndex)
                    )
                    val mediaExist = when {
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> File(data).exists()
                        else -> DocumentFile.fromSingleUri(context, contentUri)?.exists() ?: false
                    }
                    if (mediaExist) {
//                        content.add(LocalMedia(name, mimeType, date, contentUri))
                        content.add(LocalMedia(title, mimeType, date, contentUri))
                    }
                } while (it.moveToNext())
            }
        }
        return@withContext content
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun loadDownloadsFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        val content = mutableListOf<LocalMedia>()
        val cursor = context.contentResolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.DownloadColumns._ID,
                MediaStore.DownloadColumns.DATE_ADDED,
                MediaStore.DownloadColumns.MIME_TYPE,
                MediaStore.DownloadColumns.DISPLAY_NAME,
                MediaStore.DownloadColumns.DATA,
            ),
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idIndex = it.getColumnIndex(MediaStore.DownloadColumns._ID)
                    val nameIndex = it.getColumnIndex(MediaStore.DownloadColumns.DISPLAY_NAME)
                    val mimeTypeIndex = it.getColumnIndex(MediaStore.DownloadColumns.MIME_TYPE)
                    val dateIndex = it.getColumnIndex(MediaStore.DownloadColumns.DATE_ADDED)
                    val dataIndex = it.getColumnIndex(MediaStore.DownloadColumns.DATA)
                    val name = it.getString(nameIndex)
                    val mimeType = it.getString(mimeTypeIndex)
                    val date = it.getLong(dateIndex)
                    val data = it.getString(dataIndex)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        it.getLong(idIndex)
                    )
                    val mediaExist = when {
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.R -> File(data).exists()
                        else -> DocumentFile.fromSingleUri(context, contentUri)?.exists() ?: false
                    }
                    if (mediaExist) {
                        content.add(LocalMedia(name, mimeType, date, contentUri))
                    }
                } while (it.moveToNext())
            }
        }
        return@withContext content
    }

    suspend fun createTempFile(uri: Uri?): File? = withContext(ioDispatcher) {
        return@withContext uri?.let(context::createTempFile)?.also(File::deleteOnExit)
    }

    fun createTempUri(): Uri = context.createTempUri()

    fun createTempFile(): File = context.createTempFile()

    fun createTempFile(bitmap: Bitmap): File = context.createTempFile(bitmap)

    fun copyToTempUri(src: Uri): Uri {
        val result = createTempUri()
        context.copyFileUri(src, result)
        return result
    }

    fun deleteUri(uri: Uri?) = context.deleteFileUri(uri)

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