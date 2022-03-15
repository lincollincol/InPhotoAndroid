package com.linc.inphoto.ui.managepost

import com.linc.inphoto.ui.base.state.UiState

data class ManagePostUiState(
    val tags: List<String> = listOf(),
    val description: String? = null
) : UiState