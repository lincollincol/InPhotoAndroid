package com.linc.inphoto.ui.imageeditor.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class CropShape(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {

    @Parcelize
    class Rect(
        override val enabled: Boolean = true
    ) : CropShape(R.string.settings_crop_rectangle, enabled, R.drawable.ic_crop_rect)

    @Parcelize
    class Circle(
        override val enabled: Boolean = true
    ) : CropShape(R.string.settings_crop_circle, enabled, R.drawable.ic_crop_circle)

}