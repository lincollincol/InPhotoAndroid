package com.linc.inphoto.ui.audiolibrary.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class AudioLibraryIntent : Parcelable {
    @Parcelize
    data class Result(val resultKey: String) : AudioLibraryIntent()
}