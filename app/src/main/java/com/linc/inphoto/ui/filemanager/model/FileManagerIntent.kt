package com.linc.inphoto.ui.filemanager.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class FileManagerIntent : Parcelable {
    @Parcelize
    data class SingleResult(val resultKey: String) : FileManagerIntent()

    @Parcelize
    data class MultipleResult(val resultKey: String) : FileManagerIntent()
}