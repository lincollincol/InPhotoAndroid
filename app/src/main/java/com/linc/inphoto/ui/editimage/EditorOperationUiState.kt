package com.linc.inphoto.ui.editimage

import com.linc.inphoto.ui.editimage.model.EditOperation

data class EditorOperationUiState(
    val operation: EditOperation,
    val onClick: (() -> Unit)? = null
)

fun EditOperation.toUiState(onClick: () -> Unit) =
    EditorOperationUiState(this, onClick)