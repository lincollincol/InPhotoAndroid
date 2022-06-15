package com.linc.inphoto.ui.languagesettings

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.languagesettings.model.LanguageUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
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
                    .map { LanguageUiState(it, onClick = {}) }
                _uiState.update { it.copy(languages = languages) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}