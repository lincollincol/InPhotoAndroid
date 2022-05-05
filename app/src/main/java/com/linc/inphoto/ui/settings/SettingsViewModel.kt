package com.linc.inphoto.ui.settings

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.settings.model.SettingsEntry
import com.linc.inphoto.ui.settings.model.SettingsOptionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val authRepository: AuthRepository
) : BaseViewModel<SettingsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(SettingsUiState())

    fun loadSettings() {
        val settingsOptions = SettingsEntry.getEntries()
            .map { SettingsOptionUiState(it) { selectSettingsEntry(it) } }
        _uiState.update { it.copy(settingsOptions = settingsOptions) }
    }

    fun selectSettingsEntry(entry: SettingsEntry) {
        when (entry) {
            SettingsEntry.Profile -> router.navigateTo(NavScreen.ProfileSettingsScreen())
            SettingsEntry.Privacy -> NavScreen.ProfileScreen()
            SettingsEntry.Language -> NavScreen.ProfileScreen()
            SettingsEntry.Help -> NavScreen.ProfileScreen()
            SettingsEntry.SignOut -> signOut()
        }
    }

    fun closeSettings() {
        router.exit()
    }

    private fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                globalRouter.newRootScreen(NavScreen.SignInScreen())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


}