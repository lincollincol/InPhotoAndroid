package com.linc.inphoto.ui.imageeditor

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.SettingsRepository
import com.linc.inphoto.entity.AspectRatio
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageEditorViewModel @Inject constructor(
    router: Router,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<ImageEditorUiState>(router) {

    override val _uiState = MutableStateFlow(ImageEditorUiState())

    fun loadAvailableRatios() {
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

    private fun selectRatio(ratio: AspectRatio) {
        val ratioItems = uiState.value.ratioItems.map {
            it.copy(selected = it.aspectRatio == ratio)
        }
        _uiState.update { copy(ratioItems = ratioItems, currentRatio = ratio) }
    }

}