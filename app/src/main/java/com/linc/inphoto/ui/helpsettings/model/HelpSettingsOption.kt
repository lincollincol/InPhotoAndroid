package com.linc.inphoto.ui.helpsettings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R

sealed class HelpSettingsOption(
    @DrawableRes val icon: Int,
    @StringRes val title: Int
) {
    companion object {
        @JvmStatic
        fun getEntries() = listOf(PrivacyPolicy, TermsConditions)
    }

    object PrivacyPolicy : HelpSettingsOption(R.drawable.ic_privacy_policy, R.string.privacy_policy)
    object TermsConditions :
        HelpSettingsOption(R.drawable.ic_terms_and_conditions, R.string.terms_and_conditions)
}