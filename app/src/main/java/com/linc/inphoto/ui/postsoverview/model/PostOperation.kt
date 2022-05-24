package com.linc.inphoto.ui.postsoverview.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class PostOperation(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {
    companion object {
        @JvmStatic
        fun getAuthorPostOperations() = listOf(Edit, Delete, Share, Report)

        @JvmStatic
        fun getGuestPostOperations() = listOf(Share, Report)
    }

    @Parcelize
    object Edit : PostOperation(R.string.edit, true, R.drawable.ic_edit)

    @Parcelize
    object Delete : PostOperation(R.string.delete, true, R.drawable.ic_delete)

    @Parcelize
    object Share : PostOperation(R.string.share, true, R.drawable.ic_share)

    @Parcelize
    object Report : PostOperation(R.string.report, true, R.drawable.ic_report_outline)

}