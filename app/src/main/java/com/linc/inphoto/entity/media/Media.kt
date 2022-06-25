package com.linc.inphoto.entity.media

import android.net.Uri

abstract class Media {
    abstract val uri: Uri
    abstract val name: String
    abstract val mimeType: String
    abstract val extension: String
}