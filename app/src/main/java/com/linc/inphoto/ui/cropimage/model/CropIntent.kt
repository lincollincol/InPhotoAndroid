package com.linc.inphoto.ui.cropimage.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CropIntent : Parcelable {
    @Parcelize
    data class Result(val resultKey: String) : CropIntent()
}