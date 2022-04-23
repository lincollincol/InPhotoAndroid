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

val ManagePostUiState.isDescriptionValid get() = !description.isNullOrEmpty()
val ManagePostUiState.isTagsValid get() = tags.isNotEmpty()
val ManagePostUiState.isPostDataValid get() = isTagsValid && isDescriptionValid