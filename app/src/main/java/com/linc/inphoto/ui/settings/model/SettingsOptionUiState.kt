package com.linc.inphoto.ui.settings.model

data class SettingsOptionUiState(
    val entry: SettingsEntry,
    val onClick: () -> Unit
)