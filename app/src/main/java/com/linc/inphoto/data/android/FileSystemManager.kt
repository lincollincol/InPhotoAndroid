package com.linc.inphoto.data.android

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.linc.inphoto.utils.extensions.anyLet
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class FileSystemManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val FILE_PROVIDER_AUTHORITY: String get() = "${context.packageName}.provider"

    private val externalCacheDirectory: File?
        get() = context.externalCacheDir ?: context.cacheDir

    val cacheDirectoryPath: String?
        get() {
            val cacheDirectory = externalCacheDirectory ?: return null
            return cacheDirectory.absolutePath
        }

    fun uriByFile(file: File): Uri = Uri.fromFile(file)

    fun externalUriByFile(file: File): Uri = FileProvider
        .getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)

    fun fileByUri(uri: Uri?): File? = uri?.let { fileByPath(it.path) }

    fun fileByPath(path: String?): File? = path?.let { File(it) }

    fun saveFileAndGetPath(
        data: ByteArray,
        fileName: String,
        directoryName: String,
        environmentDirectory: String
    ): String? =
        with(saveFileToPublicDirectory(data, fileName, directoryName, environmentDirectory)) {
            this?.absolutePath
        }

    fun deleteFileByUri(uri: Uri?) {
        with(fileByUri(uri)) {
            this
                ?.takeIf { it.exists() }
                ?.takeIf { !it.delete() }
                ?.deleteOnExit()
                ?: Timber.e("Could not delete file: does not exist.")
        }
    }

    fun deleteFileByPath(path: String?) {
        with(fileByPath(path)) {
            this
                ?.takeIf { it.exists() }
                ?.takeIf { !it.delete() }
                ?.deleteOnExit()
                ?: Timber.e("Could not delete file: does not exist.")
        }
    }

    fun deleteFolderContent(path: String?) {
        with(fileByPath(path)) {
            this
                ?.takeIf { it.exists() }
                ?.takeIf { it.isDirectory }
                ?.takeIf { !it.delete() }
                ?.let {
                    it.listFiles()?.forEach { child ->
                        deleteFileByPath(child.path)
                    }
                }
                ?: Timber.e("Could not delete file: does not exist.")
        }
    }

    fun createUriByFilePath(filePath: String?): Uri? = fileByPath(filePath)
        ?.let { uriByFile(it) }

    fun uriByFilePath(filePath: String?): Uri? = fileByPath(filePath)
        ?.let { FileProvider.getUriForFile(context, context.packageName, it) }

    fun createUriForFile(
        fileName: String,
        suffix: String?,
        directoryName: String?,
        environmentDirectory: String?,
        onError: ((Throwable) -> Unit)?
    ): Uri? =
        createFile(fileName, suffix, directoryName, environmentDirectory, onError)
            ?.let { uriByFile(it) }

    fun externalDirectoryPath(
        directoryName: String,
        environmentDirectoryName: String
    ): String? = anyLet(directoryName, environmentDirectoryName) { (dir, environment) ->
        externalDirectory(environment, dir).absolutePath
    } ?: externalCacheDirectory?.absolutePath

    fun createFile(
        fileName: String,
        suffix: String?,
        directoryName: String?,
        environmentDirectoryName: String?,
        onError: ((Throwable) -> Unit)?
    ): File? {
        val directory = anyLet(directoryName, environmentDirectoryName) { (dir, environment) ->
            externalDirectory(environment, dir)
        }
            ?: externalCacheDirectory
            ?: return null
        return try {
            File.createTempFile(
                fileName + System.currentTimeMillis(),
                suffix,
                directory
            )
        } catch (e: IOException) {
            Timber.e("Could not create file in external storage.", e)
            onError?.invoke(e)
            null
        }
    }

    private fun saveFileToPublicDirectory(
        data: ByteArray,
        fileName: String,
        directoryName: String,
        environmentDirectory: String
    ): File? {
        val file = File(externalDirectory(environmentDirectory, directoryName), fileName)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            Timber.e("Could not create file in external storage.", e)
            return null
        }
        writeToFile(data, file)
        return file
    }

    private fun writeToFile(data: ByteArray, file: File) {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            fos.write(data)
        } catch (e: IOException) {
            Timber.e("Could not write the file to external storage.", e)
        } finally {
            try {
                fos?.flush()
                fos?.close()
            } catch (e: IOException) {
                Timber.e("Could not close the file output stream.", e)
            }
        }
    }

    private fun externalDirectory(
        environmentDirectory: String,
        directoryName: String
    ): File {
        val directory = File(
            context.getExternalFilesDir(environmentDirectory),
            directoryName
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

}
