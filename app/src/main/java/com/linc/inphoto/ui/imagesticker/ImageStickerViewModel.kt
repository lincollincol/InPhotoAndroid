package com.linc.inphoto.ui.imagesticker

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.entity.media.image.ImageSticker
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.imagesticker.model.ImageStickerIntent
import com.linc.inphoto.ui.imagesticker.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageStickerViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<ImageStickerUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ImageStickerUiState())
    private var intent: ImageStickerIntent? = null

    fun loadStickers(imageIntent: ImageStickerIntent?, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                intent = imageIntent
                val stickers = mediaRepository.loadStickers()
                    .map { sticker -> sticker.toUiState { selectSticker(sticker) } }
                _uiState.update { it.copy(image = imageUri, availableStickers = stickers) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun saveImage(imageUri: Uri?) {
        viewModelScope.launch {
            try {
                if (imageUri != null && intent is ImageStickerIntent.Result) {
                    router.sendResult(
                        (intent as ImageStickerIntent.Result).resultKey,
                        mediaRepository.convertToTempUri(imageUri)
                    )
                }
                router.exit()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun stickerAdded(sticker: ImageSticker) {
        val stickers = currentState.selectedStickers.filter { it.id != sticker.id }
        _uiState.update { it.copy(selectedStickers = stickers) }
    }

    private fun selectSticker(sticker: ImageSticker) {
        val stickers = currentState.selectedStickers.toMutableList()
            .apply { add(sticker) }
        _uiState.update { it.copy(selectedStickers = stickers) }
    }

}