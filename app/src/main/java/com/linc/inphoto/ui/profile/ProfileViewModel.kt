package com.linc.inphoto.ui.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.ui.profile.model.SourceType
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    router: Router,
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) : BaseViewModel<ProfileUiState>(router) {

    companion object {
        private const val EDIT_IMAGE_RESULT = "edit_image"
        private const val SELECT_IMAGE_RESULT = "select_image"
        private const val SELECT_SOURCE_RESULT = "select_source"
    }

    override val _uiState = MutableStateFlow(ProfileUiState())

    fun loadProfileData() = viewModelScope.launch {
        try {
            val user = userRepository.getLoggedInUser()
            _uiState.update { copy(user = user) }
        } catch (e: Exception) {

        }
    }

    fun createPost() {
        router.navigateTo(Navigation.ManagePostScreen())
    }

    fun selectImageSource(sources: List<SourceType>) {
        viewModelScope.launch {
            try {
                if (sources.all { !it.enabled }) {
                    showInfo(R.string.permissions, R.string.profile_permissions_message)
                    return@launch
                }
                router.setResultListener(SELECT_SOURCE_RESULT) { result ->
                    val selectedSource = result.safeCast<SourceType>() ?: return@setResultListener
                    handleSelectedSource(selectedSource)
                }
                router.navigateTo(
                    Navigation.ChooseOptionScreen(
                        SELECT_SOURCE_RESULT,
                        sources
                    )
                )
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
                    is SourceType.Gallery -> Navigation.GalleryScreen(
                        SELECT_IMAGE_RESULT
                    )
                    is SourceType.Camera -> Navigation.CameraScreen(SELECT_IMAGE_RESULT)
                }
                router.navigateTo(screen)

                router.setResultListener(SELECT_IMAGE_RESULT) {
                    val editorScreen = Navigation.EditImageScreen(
                        EDIT_IMAGE_RESULT,
                        it as Uri
                    )
                    router.navigateTo(editorScreen)
                }

                router.setResultListener(EDIT_IMAGE_RESULT) {
                    handleSelectedAvatar(it as? Uri)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun handleSelectedAvatar(imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val user = userRepository.updateUserAvatar(imageUri)
                _uiState.update { copy(user = user) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}