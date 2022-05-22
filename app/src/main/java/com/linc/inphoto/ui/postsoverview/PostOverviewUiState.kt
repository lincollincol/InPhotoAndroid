package com.linc.inphoto.ui.postsoverview

import com.linc.inphoto.ui.base.state.UiState

data class PostOverviewUiState(
    val posts: List<PostUiState> = listOf(),
    val initialPosition: Int? = null,
    val isLoading: Boolean = false
) : UiState