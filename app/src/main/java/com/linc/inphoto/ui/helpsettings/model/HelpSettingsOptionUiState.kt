package com.linc.inphoto.ui.helpsettings.model

data class HelpSettingsOptionUiState(
    val entry: HelpSettingsOption,
    val onClick: () -> Unit
)