package com.linc.inphoto.ui.settings.model

data class SettingsOptionUiState(
    val entry: SettingsOption,
    val onClick: () -> Unit
)