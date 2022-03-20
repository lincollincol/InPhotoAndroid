package com.linc.inphoto.ui.managepost

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState

data class ManagePostUiState(
    val imageUri: Uri? = null,
    val tags: List<String> = listOf(),
    val description: String? = null,
    val isLoading: Boolean = false,
    val isErrorsEnabled: Boolean = false
) : UiState

val ManagePostUiState.isValidDescription get() = !description.isNullOrEmpty()
val ManagePostUiState.isValidTags get() = tags.isNotEmpty()
val ManagePostUiState.isValidPostData get() = isValidTags && isValidDescription