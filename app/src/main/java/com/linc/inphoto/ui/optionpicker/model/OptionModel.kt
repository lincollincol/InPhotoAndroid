package com.linc.inphoto.ui.optionpicker.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

abstract class OptionModel(
    @StringRes val value: Int,
    open val enabled: Boolean,
    @DrawableRes val icon: Int? = null
) : Parcelable