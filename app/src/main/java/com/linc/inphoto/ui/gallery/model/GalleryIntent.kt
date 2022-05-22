package com.linc.inphoto.ui.gallery.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class GalleryIntent : Parcelable {
    @Parcelize
    data class NewAvatar(val resultKey: String) : GalleryIntent()

    @Parcelize
    object NewPost : GalleryIntent()

    @Parcelize
    object NewStory : GalleryIntent()

    @Parcelize
    data class Result(val resultKey: String) : GalleryIntent()
}