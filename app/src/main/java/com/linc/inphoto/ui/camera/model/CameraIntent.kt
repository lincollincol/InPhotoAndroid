package com.linc.inphoto.ui.camera.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CameraIntent : Parcelable {
    @Parcelize
    data class NewAvatar(val resultKey: String) : CameraIntent()
    @Parcelize
    object NewPost : CameraIntent()
}