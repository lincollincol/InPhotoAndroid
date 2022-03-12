package com.linc.inphoto.ui.profile.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class SourceType(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {

    @Parcelize
    class Camera(
        override val enabled: Boolean
    ) : SourceType(R.string.option_avatar_camera, enabled, R.drawable.ic_camera)

    @Parcelize
    class Gallery(
        override val enabled: Boolean
    ) : SourceType(R.string.option_avatar_gallery, enabled, R.drawable.ic_image)

}