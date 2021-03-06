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
        fun getAvailableSources() = listOf(Audio, Gallery, Video, Camera, Document)
    }

    @Parcelize
    object Camera : AttachmentSource(R.string.option_avatar_camera, true, R.drawable.ic_camera)

    @Parcelize
    object Gallery : AttachmentSource(R.string.option_avatar_gallery, true, R.drawable.ic_image)

    @Parcelize
    object Audio : AttachmentSource(R.string.option_audio_file, true, R.drawable.ic_audio_file)

    @Parcelize
    object Video : AttachmentSource(R.string.option_video_file, true, R.drawable.ic_video_file)

    @Parcelize
    object Document :
        AttachmentSource(R.string.option_document_file, true, R.drawable.ic_document_file)

}