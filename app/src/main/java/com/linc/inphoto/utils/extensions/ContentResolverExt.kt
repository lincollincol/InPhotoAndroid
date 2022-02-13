package com.linc.inphoto.utils.extensions

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap

fun ContentResolver.readUriBytes(uri: Uri): ByteArray? =
    openInputStream(uri)
        ?.buffered()
        ?.use { it.readBytes() }

fun ContentResolver.getUriExtension(uri: Uri): String? =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(getType(uri))