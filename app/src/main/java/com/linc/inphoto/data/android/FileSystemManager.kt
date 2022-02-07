package com.linc.inphoto.data.android

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.*
import javax.inject.Inject

class FileSystemManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun uriToFile(uri: Uri?): File? = with(context.contentResolver) {
        if (uri == null) {
            return@with null
        }
        val data = readUriBytes(uri) ?: return@with null
        val extension = getUriExtension(uri)
        return@with File(
            context.cacheDir.path,
            "${UUID.randomUUID()}.$extension"
        ).also { audio -> audio.writeBytes(data) }
    }

    private fun readUriBytes(uri: Uri) = context.contentResolver
        .openInputStream(uri)
        ?.buffered()
        ?.use { it.readBytes() }

    private fun getUriExtension(uri: Uri) = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(context.contentResolver.getType(uri))
}
