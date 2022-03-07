package com.linc.inphoto.ui.editimage.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R

sealed class EditOperation(
    @DrawableRes val icon: Int,
    @StringRes val title: Int
) {
    object Crop : EditOperation(R.drawable.ic_crop, R.string.crop)
    object Filter : EditOperation(R.drawable.ic_photo_filter, R.string.filter)
}