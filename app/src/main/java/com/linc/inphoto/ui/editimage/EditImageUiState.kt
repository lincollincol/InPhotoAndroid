package com.linc.inphoto.ui.editimage

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.editimage.model.EditorOperationUiState

data class EditImageUiState(
    val imageUri: Uri? = null,
    val editOperations: List<EditorOperationUiState> = listOf(),
) : UiState