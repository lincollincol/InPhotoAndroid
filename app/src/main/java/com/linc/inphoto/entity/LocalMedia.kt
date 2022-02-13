package com.linc.inphoto.entity

import android.net.Uri

data class LocalMedia(
    val name: String,
    val date: Long,
    val uri: Uri
)