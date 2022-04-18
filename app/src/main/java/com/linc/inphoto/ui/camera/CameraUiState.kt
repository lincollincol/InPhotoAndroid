package com.linc.inphoto.ui.camera

import com.linc.inphoto.ui.base.state.UiState

data class CameraUiState(
    val cameraPermissionsGranted: Boolean = true
) : UiState