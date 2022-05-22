package com.linc.inphoto.ui.editimage.model

data class EditorOperationUiState(
    val operation: EditOperation,
    val onClick: (() -> Unit)? = null
)

fun EditOperation.toUiState(onClick: () -> Unit) = EditorOperationUiState(this, onClick)