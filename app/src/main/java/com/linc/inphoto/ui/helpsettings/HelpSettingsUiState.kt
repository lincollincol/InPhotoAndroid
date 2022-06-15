package com.linc.inphoto.ui.helpsettings

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.helpsettings.model.HelpSettingsOptionUiState

data class HelpSettingsUiState(
    val settingsOptions: List<HelpSettingsOptionUiState> = listOf()
) : UiState
