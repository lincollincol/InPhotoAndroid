package com.linc.inphoto.ui.imagesticker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ImageStickerIntent : Parcelable {
    @Parcelize
    data class Result(val resultKey: String) : ImageStickerIntent()
}