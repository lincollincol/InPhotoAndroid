package com.linc.inphoto.ui.languagesettings

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.languagesettings.model.LanguageUiState

data class LanguageSettingsUiState(
    val languages: List<LanguageUiState> = listOf()
) : UiState
