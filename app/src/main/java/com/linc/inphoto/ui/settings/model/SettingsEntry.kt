package com.linc.inphoto.ui.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.linc.inphoto.R

sealed class SettingsEntry(
    @DrawableRes val icon: Int,
    @StringRes val title: Int
) {
    companion object {
        @JvmStatic
        fun getEntries() = listOf(Profile, Privacy, Language, Help, SignOut)
    }

    object Profile : SettingsEntry(R.drawable.ic_profile_outlined, R.string.settings_profile)
    object Privacy : SettingsEntry(R.drawable.ic_lock_privacy_user, R.string.settings_privacy)
    object Language : SettingsEntry(R.drawable.ic_language, R.string.settings_language)
    object Help : SettingsEntry(R.drawable.ic_help_outline, R.string.settings_help)
    object SignOut : SettingsEntry(R.drawable.ic_logout, R.string.settings_sign_out)
}