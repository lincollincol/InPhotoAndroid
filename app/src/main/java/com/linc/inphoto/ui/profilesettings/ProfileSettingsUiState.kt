package com.linc.inphoto.ui.profilesettings

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState

data class ProfileSettingsUiState(
    val imageUri: Uri? = null,
    val username: String? = null,
    val status: String? = null
) : UiState

val ProfileSettingsUiState.isValidUsername get() = !username.isNullOrEmpty()
