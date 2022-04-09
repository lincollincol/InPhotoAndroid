package com.linc.inphoto.ui.gallery

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<GalleryUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(GalleryUiState())

    fun loadImages(resultKey: String?) {
        viewModelScope.launch {
            try {
                val images = mediaRepository.loadGalleryImages()
                    .map { it.toUiState(onClick = { selectImage(resultKey, it.uri) }) }
                _uiState.update { copy(images = images) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelImageSelecting() {
        router.exit()
    }

    private fun selectImage(resultKey: String?, imageUri: Uri?) {
        router.exit()
        if (resultKey != null && imageUri != null) {
            router.sendResult(resultKey, mediaRepository.createTempUri(imageUri))
        }
    }

}