package com.linc.inphoto.ui.settings

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.settings.model.SettingsEntry
import com.linc.inphoto.ui.settings.model.SettingsOptionUiState
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<SettingsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(SettingsUiState())

    fun loadSettings() {
        val settingsOptions = SettingsEntry.getEntries()
            .map { SettingsOptionUiState(it) { selectSettingsEntry(it) } }
        _uiState.update { copy(settingsOptions = settingsOptions) }
    }

    fun selectSettingsEntry(entry: SettingsEntry) {
        when (entry) {
            SettingsEntry.Profile -> router.navigateTo(NavScreen.ProfileSettingsScreen())
            SettingsEntry.Privacy -> NavScreen.ProfileScreen()
            SettingsEntry.Language -> NavScreen.ProfileScreen()
            SettingsEntry.Help -> NavScreen.ProfileScreen()
            SettingsEntry.SignOut -> NavScreen.ProfileScreen()
        }
    }

    fun closeSettings() {
        router.exit()
    }

}