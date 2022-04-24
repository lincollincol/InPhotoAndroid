package com.linc.inphoto.utils.extensions

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.linc.inphoto.utils.extensions.pattern.URL_REGEX
import java.net.URL

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
