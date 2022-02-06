package com.linc.inphoto.ui.choosedialog.model

import android.os.Parcelable
import androidx.annotation.StringRes

abstract class ChooseOptionModel(
    @StringRes val value: Int,
    open val enabled: Boolean
) : Parcelable