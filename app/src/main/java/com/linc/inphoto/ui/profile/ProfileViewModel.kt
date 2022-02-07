package com.linc.inphoto.ui.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.UsersRepository
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
    private val usersRepository: UsersRepository
) : BaseViewModel<ProfileUiState>(router) {

    override val _uiState = MutableStateFlow(ProfileUiState())

    /*private val _selectGalleryImageEvent = MutableStateFlow(false)
    val selectGalleryImageEvent: Flow<Boolean> get() = _selectGalleryImageEvent
    private val _selectCameraImageEvent = MutableSharedFlow(false)
    val selectCameraImageEvent: Flow<Boolean> get() = _selectCameraImageEvent*/

    fun loadProfileData() = viewModelScope.launch {
        try {
            val user = usersRepository.getLoggedInUser()
            _uiState.update { copy(user = user) }
        } catch (e: Exception) {

        }
    }

    fun selectImageSource(sources: List<SourceType>) = viewModelScope.launch {
        try {
            router.navigateTo(Navigation.Common.CameraScreen())
            router.setResultListener(Navigation.NavResult.CAMERA_IMAGE_RESULT) { result ->
                handleContentResult(result.safeCast())
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
//        try {
//            if (sources.all { !it.enabled }) {
//                router.navigateTo(
//                    Navigation.Common.InfoMessageScreen(
//                        R.string.permissions,
//                        R.string.profile_permissions_message
//                    )
//                )
//                return@launch
//            }
//
//            router.setResultListener(Navigation.NavResult.CHOOSE_OPTION_RESULT) {
//                val selectedImageSource = it.safeCast<SourceType>()
//                when(selectedImageSource) {
//                    is SourceType.Gallery -> _selectGalleryImageEvent
//                    is SourceType.Camera -> _selectCameraImageEvent
//                    else -> return@setResultListener
//                }.value = true
//            }
//
//            router.navigateTo(Navigation.Common.ChooseOptionScreen(sources))
//        } catch (e: Exception) {
//            Timber.e(e)
//        }
    }

    private fun handleContentResult(imageUri: Uri?) {
        viewModelScope.launch {
            try {
                usersRepository.updateUserAvatar(imageUri)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}