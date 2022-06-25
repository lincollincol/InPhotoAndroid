package com.linc.inphoto.entity.media

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocalMedia(
    override val uri: Uri,
    override val name: String,
    override val mimeType: String,
    override val extension: String
) : Media(), Parcelable