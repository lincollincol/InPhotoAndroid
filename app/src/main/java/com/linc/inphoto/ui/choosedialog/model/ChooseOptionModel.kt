package com.linc.inphoto.ui.choosedialog.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChooseOptionModel(
    @StringRes val value: Int,
    val enabled: Boolean
) : Parcelable