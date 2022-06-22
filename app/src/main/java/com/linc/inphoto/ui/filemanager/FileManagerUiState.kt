package com.linc.inphoto.ui.filemanager

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.filemanager.model.FileUiState

data class FileManagerUiState(
    val files: List<FileUiState> = listOf(),
    val searchQuery: String? = null,
    val allowMultipleSelection: Boolean = false,
    val storagePermissionsGranted: Boolean = true,
    val isLoading: Boolean = false
) : UiState