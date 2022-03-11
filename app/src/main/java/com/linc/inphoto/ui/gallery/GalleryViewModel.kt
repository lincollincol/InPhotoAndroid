package com.linc.inphoto.ui.gallery

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    router: Router,
    private val mediaRepository: MediaRepository
) : BaseViewModel<GalleryUiState>(router) {

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

    private fun selectImage(resultKey: String?, imageUri: Uri?) {
        router.exit()
        if (resultKey != null && imageUri != null) {
            router.sendResult(resultKey, mediaRepository.createTempUri(imageUri))
        }
    }

}