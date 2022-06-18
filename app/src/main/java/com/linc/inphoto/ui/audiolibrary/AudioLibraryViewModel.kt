package com.linc.inphoto.ui.audiolibrary

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.ui.audiolibrary.model.AudioUiState
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
    private var audios: List<AudioUiState> = listOf()

    fun loadAudioList() {
        viewModelScope.launch {
            try {
                audios = mediaRepository.loadAudioFiles()
                    .map { it.toUiState { } }
                _uiState.update { it.copy(audios = audios) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        val foundAudios = audios.filter { it.name.contains(query, ignoreCase = true) }
        _uiState.update { it.copy(searchQuery = query, audios = foundAudios) }
    }

}