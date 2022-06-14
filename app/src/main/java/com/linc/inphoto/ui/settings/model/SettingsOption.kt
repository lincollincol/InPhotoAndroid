package com.linc.inphoto.ui.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R

sealed class SettingsOption(
    @DrawableRes val icon: Int,
    @StringRes val title: Int
) {
    companion object {
        @JvmStatic
        fun getEntries() = listOf(Profile, Language, Help, SignOut)
    }

    object Profile : SettingsOption(R.drawable.ic_profile_outlined, R.string.settings_profile)
    object Language : SettingsOption(R.drawable.ic_language, R.string.settings_language)
    object Help : SettingsOption(R.drawable.ic_help_outline, R.string.settings_help)
    object SignOut : SettingsOption(R.drawable.ic_logout, R.string.settings_sign_out)
}