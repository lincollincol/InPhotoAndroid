package com.linc.inphoto.ui.camera

import android.net.Uri
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<CameraUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(CameraUiState())

    fun handleCapturedImage(intent: CameraIntent?, uri: Uri?) {
        val imageUri = uri?.let(mediaRepository::convertToTempUri) ?: return
        val editorIntent = when (intent) {
            is CameraIntent.NewAvatar -> EditorIntent.NewAvatar(intent.resultKey)
            is CameraIntent.NewPost -> EditorIntent.NewPost
            else -> return
        }
        router.navigateTo(NavScreen.EditImageScreen(editorIntent, imageUri))
    }

    fun updateCameraPermissions(allowed: Boolean) {
        _uiState.update { copy(cameraPermissionsGranted = allowed) }
    }

    fun openSettings() {
        router.navigateTo(NavScreen.AppSettingsScreen())
    }

}