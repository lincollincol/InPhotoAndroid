package com.linc.inphoto.ui.audiolibrary.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class AudioLibraryIntent : Parcelable {
    @Parcelize
    data class SingleResult(val resultKey: String) : AudioLibraryIntent()

    @Parcelize
    data class MultipleResult(val resultKey: String) : AudioLibraryIntent()
}