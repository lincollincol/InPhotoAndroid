package com.linc.inphoto.ui.audiolibrary

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.audiolibrary.model.toUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AudioLibraryViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val mediaRepository: MediaRepository
) : BaseViewModel<AudioLibraryUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(AudioLibraryUiState())

    fun loadAudioList() {
        viewModelScope.launch {
            try {
                val audios = mediaRepository.loadAudioFiles()
                    .map { it.toUiState { } }
                _uiState.update { it.copy(audios = audios) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}