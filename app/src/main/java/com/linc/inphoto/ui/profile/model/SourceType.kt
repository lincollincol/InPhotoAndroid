package com.linc.inphoto.ui.profile.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import kotlinx.android.parcel.Parcelize

sealed class SourceType(
    @StringRes value: Int,
    enabled: Boolean
) : ChooseOptionModel(value, enabled), Parcelable {

    @Parcelize
    class Camera(override val enabled: Boolean) : SourceType(R.string.camera, enabled)

    @Parcelize
    class Gallery(override val enabled: Boolean) : SourceType(R.string.gallery, enabled)

}