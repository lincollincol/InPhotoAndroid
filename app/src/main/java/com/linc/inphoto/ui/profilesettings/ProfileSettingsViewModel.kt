package com.linc.inphoto.ui.profilesettings

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.profile.model.ImageSource
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : BaseViewModel<ProfileSettingsUiState>(navContainerHolder) {

    companion object {
        private const val CANCEL_CHANGES_RESULT = "cancel_changes_result"
        private const val IMAGE_SOURCE_RESULT = "image_source_result"
        private const val PROFILE_AVATAR_RESULT = "profile_avatar_result"
        private const val PROFILE_HEADER_RESULT = "profile_header_result"
    }

    override val _uiState = MutableStateFlow(ProfileSettingsUiState())
    private var user: User? = null

    override fun onBackPressed() {
        cancelProfileUpdate()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            try {
                user = userRepository.getLoggedInUser()
                _uiState.update {
                    copy(
                        avatarUri = user?.avatarUrl?.toUri(),
                        headerUri = user?.headerUrl?.toUri(),
                        gender = user?.gender,
                        username = user?.name,
                        status = user?.status
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
                val state = _uiState.value

                if (!state.isValidUsername)
                    return@launch showDataRequired()

                if (state.avatarUri != user?.avatarUrl?.toUri()) {
                    state.avatarUri?.let { userRepository.updateUserAvatar(it) }
                }
                if (state.headerUri != user?.headerUrl?.toUri()) {
                    state.headerUri?.let { userRepository.updateUserHeader(it) }
                }
                if (state.username.toString() != user?.name) {
                    userRepository.updateUserName(state.username.toString())
                }
                if (state.status.toString() != user?.status) {
                    userRepository.updateUserStatus(state.status.toString())
                }
                if (state.gender != user?.gender) {
                    userRepository.updateUserGender(state.gender)
                }
                router.exit()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelProfileUpdate() {
        val state = uiState.value
        val updateStatuses = listOf(
            state.avatarUri != user?.avatarUrl?.toUri(),
            state.headerUri != user?.headerUrl?.toUri(),
            state.username.toString() != user?.name,
            state.status.toString() != user?.status,
            state.gender != user?.gender
        )
        when {
            updateStatuses.any { updated -> updated } -> {
                with(router) {
                    showDialog(
                        NavScreen.ConfirmDialogScreen(
                            CANCEL_CHANGES_RESULT,
                            R.string.settings_cancel_changes_title,
                            R.string.settings_cancel_changes_description,
                        )
                    )
                    setResultListener(CANCEL_CHANGES_RESULT) { result ->
                        if (result as Boolean) router.exit()
                    }
                }
            }
            else -> router.exit()
        }
    }

    fun updateAvatar() {
        selectImageSource(PROFILE_AVATAR_RESULT) { avatarUri ->
            _uiState.update { copy(avatarUri = avatarUri) }
        }
    }

    fun updateHeader() {
        selectImageSource(PROFILE_HEADER_RESULT) { headerUri ->
            _uiState.update { copy(headerUri = headerUri) }
        }
    }

    fun randomAvatar() {
        viewModelScope.launch {
            try {
                val avatarUri = mediaRepository.loadRandomUserAvatar(_uiState.value.gender)?.toUri()
                _uiState.update { copy(avatarUri = avatarUri) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun randomHeader() {
        viewModelScope.launch {
            try {
                val headerUri = mediaRepository.loadRandomUserHeader()?.toUri()
                _uiState.update { copy(headerUri = headerUri) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun updateUsername(name: String?) {
        _uiState.update { copy(username = name) }
    }

    fun updateStatus(status: String?) {
        _uiState.update { copy(status = status) }
    }

    fun updateGender(gender: Gender) {
        _uiState.update { copy(gender = gender) }
    }

    private fun selectImageSource(resultKey: String, action: (Uri?) -> Unit) {
        router.setResultListener(IMAGE_SOURCE_RESULT) { result ->
            val imageSource = result.safeCast<ImageSource>() ?: return@setResultListener
            val screen = when (imageSource) {
                is ImageSource.Gallery ->
                    NavScreen.GalleryScreen(GalleryIntent.NewAvatar(resultKey))
                is ImageSource.Camera -> NavScreen.CameraScreen(CameraIntent.NewAvatar(resultKey))
            }
            router.navigateTo(screen)
        }
        router.setResultListener(resultKey) { result ->
            action(result.safeCast<Uri>())
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            IMAGE_SOURCE_RESULT,
            ImageSource.getAvailableSources()
        )
        router.showDialog(pickerScreen)
    }

    private fun showDataRequired() {
        router.showDialog(
            NavScreen.InfoMessageScreen(
                R.string.settings_invalid_data,
                R.string.settings_invalid_data_description,
            )
        )
    }

}