package com.linc.inphoto.ui.camera

import android.net.Uri
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    router: Router,
    private val mediaRepository: MediaRepository
) : BaseViewModel<CameraUiState>(router) {

    override val _uiState = MutableStateFlow(CameraUiState())

    fun handleCapturedImage(resultKey: String?, imageUri: Uri?) {
        router.exit()
        if (resultKey != null && imageUri != null) {
            router.sendResult(resultKey, mediaRepository.convertToTempUri(imageUri))
        }
    }

}