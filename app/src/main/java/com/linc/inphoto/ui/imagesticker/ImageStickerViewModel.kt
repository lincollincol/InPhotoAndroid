package com.linc.inphoto.ui.imagesticker

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.entity.media.image.ImageSticker
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.imagesticker.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ImageStickerViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<ImageStickerUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ImageStickerUiState())

    fun loadStickers(imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val stickers = listOf(
                    ImageSticker(
                        UUID.randomUUID().toString(),
                        Uri.parse("https://img.icons8.com/color/480/firebase.png")
                    ),
                    ImageSticker(
                        UUID.randomUUID().toString(),
                        Uri.parse("https://iconape.com/wp-content/png_logo_vector/android-robot-head.png")
                    ),
                    ImageSticker(
                        UUID.randomUUID().toString(),
                        Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/Kotlin_Icon_2021.svg/1024px-Kotlin_Icon_2021.svg.png")
                    ),
                    ImageSticker(
                        UUID.randomUUID().toString(),
                        Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/800px-Google_%22G%22_Logo.svg.png")
                    ),
                    ImageSticker(
                        UUID.randomUUID().toString(),
                        Uri.parse("https://icons.iconarchive.com/icons/wikipedia/flags/1024/UA-Ukraine-Flag-icon.png")
                    ),
                ).map { sticker ->
                    sticker.toUiState { selectSticker(sticker) }
                }
                _uiState.update { it.copy(image = imageUri, availableStickers = stickers) }
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