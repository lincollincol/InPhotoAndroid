package com.linc.inphoto.ui.home.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import kotlinx.android.parcel.Parcelize

sealed class HomePostOperation(
    @StringRes value: Int,
    enabled: Boolean,
    @DrawableRes icon: Int
) : OptionModel(value, enabled, icon), Parcelable {
    companion object {
        @JvmStatic
        fun getAuthorPostOperations() = listOf(Share, Report)

        @JvmStatic
        fun getGuestPostOperations() = listOf(Share, Report)
    }

    @Parcelize
    object Share : HomePostOperation(R.string.share, true, R.drawable.ic_share)

    @Parcelize
    object Report : HomePostOperation(R.string.report, true, R.drawable.ic_report_outline)

}