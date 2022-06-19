package com.linc.inphoto.entity.media

import android.net.Uri
import java.io.Serializable

data class LocalMedia(
    val name: String,
    val mimeType: String,
    val date: Long,
    val uri: Uri
) : Serializable