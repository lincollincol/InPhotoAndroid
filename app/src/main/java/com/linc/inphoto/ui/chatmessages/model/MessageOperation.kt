package com.linc.inphoto.ui.chatmessages.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class MessageOperation(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {
    companion object {
        @JvmStatic
        fun getMessageOperations() = listOf(Edit, Delete)
    }

    @Parcelize
    object Edit : MessageOperation(R.string.edit, true, R.drawable.ic_edit)

    @Parcelize
    object Delete : MessageOperation(R.string.delete, true, R.drawable.ic_delete)

}