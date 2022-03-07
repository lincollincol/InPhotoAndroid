package com.linc.inphoto.ui.editimage

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.editimage.model.EditOperation

data class EditImageUiState(
    val editOperations: List<EditOperation> = listOf(),
    val currentEditOperation: EditOperation? = null,
) : UiState