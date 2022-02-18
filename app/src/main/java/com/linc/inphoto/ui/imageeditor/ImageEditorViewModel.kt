package com.linc.inphoto.ui.imageeditor

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ImageEditorViewModel @Inject constructor(
    router: Router
) : BaseViewModel<ImageEditorUiState>(router) {

    override val _uiState = MutableStateFlow(ImageEditorUiState())

    fun loadAvailableRatios() {
        viewModelScope.launch {
            try {
                val ratioItems = listOf(
                    Pair(3, 2),
                    Pair(4, 3),
                    Pair(5, 4),
                    Pair(1, 1),
                    Pair(4, 5),
                    Pair(3, 4),
                    Pair(2, 3)
                ).map { RatioUiState(it.first, it.second, onClick = { selectRatio(it) }) }
                _uiState.update { copy(ratioItems = ratioItems) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun selectRatio(ratio: Pair<Int, Int>) {

    }

}