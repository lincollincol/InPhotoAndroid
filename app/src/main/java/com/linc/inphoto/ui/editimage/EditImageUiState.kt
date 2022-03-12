package com.linc.inphoto.ui.editimage

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState

data class EditImageUiState(
    val imageUri: Uri? = null,
    val editOperations: List<EditorOperationUiState> = listOf(),
) : UiState