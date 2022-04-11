package com.linc.inphoto.ui.editimage.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class EditorIntent : Parcelable {
    @Parcelize
    object NewAvatar : EditorIntent()
    @Parcelize
    object NewPost : EditorIntent()
}