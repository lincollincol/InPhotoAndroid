package com.linc.inphoto.entity.media

import android.net.Uri

data class RemoteMedia(
    override val uri: Uri,
    override val name: String,
    override val mimeType: String,
    override val extension: String
) : Media() {
}