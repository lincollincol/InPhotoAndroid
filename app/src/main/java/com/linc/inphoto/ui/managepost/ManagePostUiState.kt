package com.linc.inphoto.ui.managepost

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState

data class ManagePostUiState(
    val postId: String? = null,
    val imageUri: Uri? = null,
    val tags: Set<String> = setOf(),
    val description: String? = null,
    val isLoading: Boolean = false,
    val isErrorsEnabled: Boolean = false
) : UiState

val ManagePostUiState.isValidDescription get() = !description.isNullOrEmpty()
val ManagePostUiState.isValidTags get() = tags.isNotEmpty()
val ManagePostUiState.isValidPostData get() = isValidTags && isValidDescription