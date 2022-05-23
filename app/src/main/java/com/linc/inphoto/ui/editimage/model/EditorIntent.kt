package com.linc.inphoto.ui.editimage.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class EditorIntent : Parcelable {
    @Parcelize
    data class NewAvatar(val resultKey: String) : EditorIntent()

    @Parcelize
    object NewStory : EditorIntent()

    @Parcelize
    object NewPost : EditorIntent()
}