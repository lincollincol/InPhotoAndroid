package com.linc.inphoto.ui.languagesettings

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.languagesettings.model.LanguageUiState
import java.util.*

data class LanguageSettingsUiState(
    val languages: List<LanguageUiState> = listOf(),
    val newLocale: Locale? = null
) : UiState
