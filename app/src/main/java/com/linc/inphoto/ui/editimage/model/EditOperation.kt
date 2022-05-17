package com.linc.inphoto.ui.editimage.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R

sealed class EditOperation(
    @DrawableRes val icon: Int,
    @StringRes val title: Int
) {

    companion object {
        fun getEditorOperations() = listOf(Crop, Sticker, Text, Filter)
    }

    object Crop : EditOperation(R.drawable.ic_crop, R.string.crop)
    object Text : EditOperation(R.drawable.ic_text_field, R.string.text)
    object Sticker : EditOperation(R.drawable.ic_sticker, R.string.sticker)
    object Filter : EditOperation(R.drawable.ic_photo_filter, R.string.filter)
}