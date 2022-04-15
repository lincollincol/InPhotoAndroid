package com.linc.inphoto.ui.profilesettings

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository
) : BaseViewModel<ProfileSettingsUiState>(navContainerHolder) {

    companion object {
        private const val CANCEL_CHANGES_RESULT = "cancel_changes_result"
    }

    override val _uiState = MutableStateFlow(ProfileSettingsUiState())
    private var user: User? = null

    fun loadProfileData() {
        viewModelScope.launch {
            try {
                user = userRepository.getLoggedInUser()
                _uiState.update {
                    copy(
                        imageUri = user?.avatarUrl?.toUri(),
                        username = StringBuilder(user?.name.orEmpty()),
                        status = StringBuilder(user?.status.orEmpty())
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun saveProfileData() {
        viewModelScope.launch {
            try {
                val state = uiState.value
                if(state.imageUri != user?.avatarUrl?.toUri()) {
                    userRepository.updateUserAvatar(state.imageUri)
                }
                if(state.username.toString() != user?.name) {
                    userRepository.updateUserName(state.username.toString())
                }
                if(state.status.toString() != user?.status) {
                    userRepository.updateUserStatus(state.status.toString())
                }
                router.exit()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelProfileUpdate() {
        val state = uiState.value
        when {
            state.imageUri != user?.avatarUrl?.toUri() ||
            state.username.toString() != user?.name ||
            state.status.toString() != user?.status -> {
                with(router) {
                    showDialog(
                        NavScreen.ConfirmDialogScreen(
                            CANCEL_CHANGES_RESULT,
                            R.string.settings_cancel_changes_title,
                            R.string.settings_cancel_changes_description,
                        )
                    )
                    setResultListener(CANCEL_CHANGES_RESULT) { result ->
                        if(result as Boolean) router.exit()
                    }
                }
            }
            else -> router.exit()
        }
    }

    fun updateUsername(name: String?) {
        _uiState.value.username?.update(name.orEmpty())
    }

    fun updateStatus(status: String?) {
        _uiState.value.status?.update(status.orEmpty())
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        cancelProfileUpdate()
    }

}