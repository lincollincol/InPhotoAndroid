package com.linc.inphoto.ui.camera

import android.net.Uri
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    router: Router
) : BaseViewModel<CameraUiState>(router) {

    override val _uiState = MutableStateFlow(CameraUiState())

    fun handleCapturedImage(imageUri: Uri?) {
        router.exit()
        if (imageUri != null) {
            router.sendResult(
                Navigation.NavResult.CAMERA_IMAGE_RESULT,
                imageUri
            )
        }
    }

}