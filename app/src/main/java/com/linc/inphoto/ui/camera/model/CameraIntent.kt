package com.linc.inphoto.ui.camera.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CameraIntent : Parcelable {
    @Parcelize
    object NewAvatar : CameraIntent()
    @Parcelize
    object NewPost : CameraIntent()
}