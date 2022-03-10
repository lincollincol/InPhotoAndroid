package com.linc.inphoto.ui.cropimage

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.entity.AspectRatio
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.cropimage.model.CropShape
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CropImageViewModel @Inject constructor(
    router: Router,
    private val settingsRepository: SettingsRepository,
    private val mediaRepository: MediaRepository
) : BaseViewModel<CropImageUiState>(router) {

    companion object {
        private const val CHOOSE_SHAPE_RESULT = "shape_result"
    }

    override val _uiState = MutableStateFlow(CropImageUiState())

    fun prepareCrop() {
        viewModelScope.launch {
            try {
                val ratioItems = settingsRepository.loadAspectRatios()
                    .map { RatioUiState(it, onClick = { selectRatio(it) }) }
                _uiState.update { copy(ratioItems = ratioItems) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun selectCropShape() {
        val shapeOptions = listOf(CropShape.Rect(), CropShape.Circle())
        router.setResultListener(CHOOSE_SHAPE_RESULT) { result ->
            val selectedShape = result.safeCast<CropShape>() ?: return@setResultListener
            _uiState.update { copy(cropShape = selectedShape) }
        }
        router.navigateTo(Navigation.Common.ChooseOptionScreen(CHOOSE_SHAPE_RESULT, shapeOptions))
    }

    fun changeRatioState(isFixed: Boolean) {
        val ratioItems = _uiState.value.ratioItems.map { it.copy(selected = false) }
        _uiState.update {
            copy(isFixedAspectRatio = isFixed, ratioItems = ratioItems, currentRatio = null)
        }
    }

    fun saveCroppedImage(resultKey: String?, imageBitmap: Bitmap?) {
        router.exit()
        if (resultKey != null && imageBitmap != null) {
            router.sendResult(
                resultKey,
                mediaRepository.createTempUri(imageBitmap)
            )
        }
    }

    fun cancelCropping() {
        router.exit()
    }

    private fun selectRatio(ratio: AspectRatio) {
        val ratioItems = uiState.value.ratioItems.map {
            it.copy(selected = it.aspectRatio == ratio)
        }
        _uiState.update { copy(ratioItems = ratioItems, currentRatio = ratio) }
    }

}