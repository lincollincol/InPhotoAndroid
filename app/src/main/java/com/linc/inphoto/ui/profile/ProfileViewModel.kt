package com.linc.inphoto.ui.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.ui.profile.model.SourceType
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.setResultListener
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    router: Router,
    private val userRepository: UserRepository
) : BaseViewModel<ProfileUiState>(router) {

    override val _uiState = MutableStateFlow(ProfileUiState())

    fun loadProfileData() = viewModelScope.launch {
        try {
            val user = userRepository.getLoggedInUser()
            _uiState.update { copy(user = user) }
        } catch (e: Exception) {

        }
    }

    fun selectImageSource(sources: List<SourceType>) {
        viewModelScope.launch {
            try {
                if (sources.all { !it.enabled }) {
                    showInfo(R.string.permissions, R.string.profile_permissions_message)
                    return@launch
                }
                router.setResultListener(Navigation.NavResult.CHOOSE_OPTION_RESULT) { result ->
                    val selectedSource = result.safeCast<SourceType>() ?: return@setResultListener
                    handleSelectedSource(selectedSource)
                }
                router.navigateTo(Navigation.Common.ChooseOptionScreen(sources))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun handleSelectedSource(source: SourceType) {
        viewModelScope.launch {
            try {
                // Navigate to source screen
                val screen = when (source) {
                    is SourceType.Gallery -> Navigation.Common.GalleryScreen()
                    is SourceType.Camera -> Navigation.Common.CameraScreen()
                }
                router.navigateTo(screen)

                // Wait for selected image and update avatar
                router.setResultListener<Uri>(Navigation.NavResult.CAMERA_IMAGE_RESULT) {
                    userRepository.updateUserAvatar(it.getOrNull())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}