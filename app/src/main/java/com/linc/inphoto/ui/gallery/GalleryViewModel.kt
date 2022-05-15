package com.linc.inphoto.ui.gallery

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<GalleryUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(GalleryUiState())
    private var intent: GalleryIntent? = null

    fun loadImages(intent: GalleryIntent?) {
        this.intent = intent
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(galleryPermissionsGranted = true) }
                if (currentState.images.isNotEmpty()) {
                    return@launch
                }
                val images = mediaRepository.loadGalleryImages()
                    .map { it.toUiState(onClick = { selectImage(intent, it.uri) }) }
                _uiState.update { it.copy(images = images) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun permissionDenied() {
        _uiState.update { it.copy(galleryPermissionsGranted = false) }
    }

    fun openSettings() {
        router.navigateTo(NavScreen.AppSettingsScreen())
    }

    fun cancelImageSelecting() {
        router.exit()
    }

    private fun selectImage(intent: GalleryIntent?, imageUri: Uri) {
        val editorIntent = when (intent) {
            is GalleryIntent.NewPost -> EditorIntent.NewPost
            is GalleryIntent.NewAvatar -> EditorIntent.NewAvatar(intent.resultKey)
            is GalleryIntent.Result -> {
                return router.run {
                    sendResult(intent.resultKey, imageUri)
                    exit()
                }
            }
            else -> return
        }
        router.navigateTo(NavScreen.EditImageScreen(editorIntent, imageUri))
    }

}