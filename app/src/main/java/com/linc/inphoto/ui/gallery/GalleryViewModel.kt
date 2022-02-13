package com.linc.inphoto.ui.gallery

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.ContentRepository
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
    private val contentRepository: ContentRepository
) : BaseViewModel<GalleryUiState>(router) {

    override val _uiState = MutableStateFlow(GalleryUiState())

    fun loadImages() {
        viewModelScope.launch {
            try {
                val images = contentRepository.loadGalleryImages()
                _uiState.update { copy(images = images) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}