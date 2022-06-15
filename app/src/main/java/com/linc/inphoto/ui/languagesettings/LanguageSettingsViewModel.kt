package com.linc.inphoto.ui.languagesettings

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.entity.settings.Localization
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.languagesettings.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.utils.extensions.mapIf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LanguageSettingsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<LanguageSettingsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(LanguageSettingsUiState())

    fun loadLanguages() {
        viewModelScope.launch {
            try {
                val languages = settingsRepository.loadAvailableLanguages()
                    .map(::getLanguageUiState)
                _uiState.update { it.copy(languages = languages) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun localeShown() {
        _uiState.update { it.copy(newLocale = null) }
    }

    private fun selectLocale(localization: Localization) {
        val languages = currentState.languages
            .map { it.copy(isCurrentLocale = false) }
            .mapIf(
                condition = { it.locale.equals(localization.locale) },
                transform = { it.copy(isCurrentLocale = true) }
            )
        _uiState.update {
            it.copy(newLocale = localization.locale, languages = languages)
        }
    }

    private fun getLanguageUiState(localization: Localization) =
        localization.toUiState { selectLocale(localization) }
}