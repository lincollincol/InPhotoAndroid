package com.linc.inphoto.ui.postsoverview

import com.linc.inphoto.ui.base.state.UiState

data class PostOverviewUiState(
    val posts: List<PostUiState> = listOf(),
//    val initialPost: PostUiState? = null
    val initialPosition: Int? = null
) : UiState