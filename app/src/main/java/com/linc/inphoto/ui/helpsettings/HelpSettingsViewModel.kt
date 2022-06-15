package com.linc.inphoto.ui.helpsettings

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.helpsettings.model.HelpSettingsOption
import com.linc.inphoto.ui.helpsettings.model.HelpSettingsOptionUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HelpSettingsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val settingsRepository: SettingsRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<HelpSettingsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(HelpSettingsUiState())

    fun loadHelpOptions() {
        viewModelScope.launch {
            try {
                val helpOptions = HelpSettingsOption.getEntries()
                    .map { option ->
                        HelpSettingsOptionUiState(
                            option,
                            onClick = { selectHelpOption(option) }
                        )
                    }
                _uiState.update { it.copy(settingsOptions = helpOptions) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun selectHelpOption(option: HelpSettingsOption) {
        viewModelScope.launch {
            try {
                val pageUrl = when (option) {
                    HelpSettingsOption.PrivacyPolicy -> settingsRepository.loadPrivacyPolicyUrl()
                    HelpSettingsOption.TermsConditions -> settingsRepository.loadTermsAndConditionsUrl()
                }
                router.navigateTo(
                    NavScreen.WebPageScreen(
                        resourceProvider.getString(option.title),
                        pageUrl
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}