package com.linc.inphoto.ui.managepost

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState

data class ManagePostUiState(
    val imageUri: Uri? = null,
    val tags: List<String> = listOf(),
    val description: String? = null
) : UiState