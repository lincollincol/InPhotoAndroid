package com.linc.inphoto.utils.extensions

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.linc.inphoto.utils.extensions.pattern.URL_REGEX
import jodd.net.MimeTypes
import java.net.URL
import java.util.*

fun Uri.isUrl() = Regex(URL_REGEX).matches(toString())

fun Uri.getUrlBytes() = URL(toString()).openStream()
    ?.buffered()
    ?.use { it.readBytes() }


fun Uri.getUrlExtension(): String? = MimeTypeMap.getFileExtensionFromUrl(toString())

fun Uri.getFileBytes(content: Context): ByteArray? =
    content.contentResolver.openInputStream(this)
        ?.buffered()
        ?.use { it.readBytes() }

fun Uri.getFileExtension(context: Context): String? = MimeTypeMap.getSingleton()
    .getMimeTypeFromExtension(context.contentResolver.getType(this))

fun Uri.getMimeType(context: Context): String? {
    var mimeType: String? = null
    if (scheme == ContentResolver.SCHEME_CONTENT) {
        mimeType = context.contentResolver.getType(this)
    }
    val ext = MimeTypeMap.getFileExtensionFromUrl(toString()).lowercase(Locale.US)
    if (scheme == ContentResolver.SCHEME_FILE && mimeType.isNullOrEmpty()) {
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
    }
    if (mimeType.isNullOrEmpty()) {
        mimeType = MimeTypes.getMimeType(ext)
    }
    if (mimeType.isNullOrEmpty()) {
        MimeTypes.getMimeType(ext)
    }
    return mimeType
}

fun Uri.getMimeTypePrefix(context: Context): String? =
    getMimeType(context)?.substringBefore("/")