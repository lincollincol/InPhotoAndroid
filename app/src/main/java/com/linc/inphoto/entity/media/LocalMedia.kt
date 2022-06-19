package com.linc.inphoto.entity.media

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocalMedia(
    val name: String,
    val mimeType: String,
    val date: Long,
    val uri: Uri
) : Parcelable