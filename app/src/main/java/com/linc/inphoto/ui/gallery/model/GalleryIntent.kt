package com.linc.inphoto.ui.gallery.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class GalleryIntent : Parcelable {
    @Parcelize
    object NewAvatar : GalleryIntent()
    @Parcelize
    object NewPost : GalleryIntent()
}