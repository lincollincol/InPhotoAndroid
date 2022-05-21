package com.linc.inphoto.ui.tagposts

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.tagposts.model.TagPostUiState

data class TagPostsUiState(
    val name: String? = null,
    val postPreviewUrl: String? = null,
    val postsCount: Int = 0,
    val posts: List<TagPostUiState> = listOf(),
    val isLoading: Boolean = false
) : UiState

val TagPostsUiState.isTagInfoValid get() = name != null