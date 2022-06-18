package com.linc.inphoto.ui.audiolibrary

import com.linc.inphoto.ui.audiolibrary.model.AudioUiState
import com.linc.inphoto.ui.base.state.UiState

data class AudioLibraryUiState(
    val audios: List<AudioUiState> = listOf()
) : UiState