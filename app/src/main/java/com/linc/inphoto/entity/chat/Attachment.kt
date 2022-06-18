package com.linc.inphoto.entity.chat

import android.net.Uri

data class Attachment(
    val id: String,
    val uri: Uri,
    val type: String
) {

}