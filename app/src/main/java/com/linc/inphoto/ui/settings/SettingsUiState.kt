package com.linc.inphoto.ui.settings

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.settings.model.SettingsOptionUiState

data class SettingsUiState(
    val settingsOptions: List<SettingsOptionUiState> = listOf()
) : UiState