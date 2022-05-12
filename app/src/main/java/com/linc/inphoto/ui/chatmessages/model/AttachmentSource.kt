package com.linc.inphoto.ui.chatmessages.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class AttachmentSource(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {

    companion object {
        @JvmStatic
        fun getAvailableSources() = listOf(Gallery, Camera)
    }

    @Parcelize
    object Camera : AttachmentSource(R.string.option_avatar_camera, true, R.drawable.ic_camera)

    @Parcelize
    object Gallery : AttachmentSource(R.string.option_avatar_gallery, true, R.drawable.ic_image)

}