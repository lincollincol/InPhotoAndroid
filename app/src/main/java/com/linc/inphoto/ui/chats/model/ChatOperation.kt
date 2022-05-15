package com.linc.inphoto.ui.chats.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class ChatOperation(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {
    companion object {
        @JvmStatic
        fun getChatOperations() = listOf(Delete, Mute, Pin)
    }

    @Parcelize
    object Delete : ChatOperation(R.string.delete, true, R.drawable.ic_delete)

    @Parcelize
    object Mute : ChatOperation(R.string.mute, true, R.drawable.ic_mute)

    @Parcelize
    object Pin : ChatOperation(R.string.pin, true, R.drawable.ic_pin)

}