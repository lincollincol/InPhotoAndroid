package com.linc.inphoto.ui.gallery

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
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
    private var intent: GalleryIntent? = null

    fun loadImages(intent: GalleryIntent?) {
        this.intent = intent
        viewModelScope.launch {
            try {
                val images = mediaRepository.loadGalleryImages()
                    .map { it.toUiState(onClick = { selectImage(it.uri) }) }
                _uiState.update { copy(images = images) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelImageSelecting() {
        router.exit()
    }

    private fun selectImage(imageUri: Uri) {
        val intent = when (intent) {
            GalleryIntent.NewAvatar -> EditorIntent.NewAvatar
            GalleryIntent.NewPost -> EditorIntent.NewPost
            else -> return
        }
        router.navigateTo(NavScreen.EditImageScreen(intent, imageUri))
    }

}