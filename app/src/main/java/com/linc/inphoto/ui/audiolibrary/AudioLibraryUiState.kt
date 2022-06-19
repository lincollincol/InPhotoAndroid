package com.linc.inphoto.ui.audiolibrary

import com.linc.inphoto.ui.audiolibrary.model.AudioUiState
import com.linc.inphoto.ui.base.state.UiState

data class AudioLibraryUiState(
    val audios: List<AudioUiState> = listOf(),
    val searchQuery: String? = null,
    val allowMultipleSelection: Boolean = false,
    val storagePermissionsGranted: Boolean = true,
    val isLoading: Boolean = false
) : UiState