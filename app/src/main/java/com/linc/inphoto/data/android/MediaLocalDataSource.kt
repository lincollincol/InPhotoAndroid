package com.linc.inphoto.data.android

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import com.linc.inphoto.data.android.model.MediaFile
import com.linc.inphoto.entity.LocalMedia
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

    // TODO: refactor this functions
    // refactor to single function
    suspend fun loadDCIMFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        val content = mutableListOf<LocalMedia>()
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Audio.Media.MIME_TYPE,
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
                    val mimeTypeIndex = it.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)
                    val dateIndex = it.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val dataIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    val name = it.getString(nameIndex)
                    val mimeType = it.getString(mimeTypeIndex)
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
                        content.add(LocalMedia(name, mimeType, date, contentUri))
                    }
                } while (it.moveToNext())
            }
        }
        return@withContext content
    }

    suspend fun loadAudioFiles(): List<LocalMedia> = withContext(ioDispatcher) {
        val content = mutableListOf<LocalMedia>()
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
            ),
            null,
            null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        )
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val idIndex = it.getColumnIndex(MediaStore.Audio.Media._ID)
                    val nameIndex = it.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                    val mimeTypeIndex = it.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)
                    val dateIndex = it.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
                    val dataIndex = it.getColumnIndex(MediaStore.Audio.Media.DATA)
                    val name = it.getString(nameIndex)
                    val mimeType = it.getString(mimeTypeIndex)
                    val date = it.getLong(dateIndex)
                    val data = it.getString(dataIndex)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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

    @Deprecated("Future release feature")
    suspend fun createTempMediaFile(uri: Uri?): MediaFile? = withContext(ioDispatcher) {
        uri ?: return@withContext null
        val file = createTempFile(uri) ?: return@withContext null
        val mimeType = uri.getMimeTypePrefix(context) ?: return@withContext null
        return@withContext when {
            mimeType.isAudioMimeType() ->
                MediaFile.Audio(
                    file,
                    mimeType,
                    amplituda.processAudio(file).get().amplitudesAsList()
                )
            mimeType.isImageMimeType() -> MediaFile.Image(file, mimeType)
            mimeType.isVideoMimeType() -> MediaFile.Video(file, mimeType)
            mimeType.isDocMimeType() -> MediaFile.Document(file, mimeType)
            else -> null
        }
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