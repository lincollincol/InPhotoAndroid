package com.linc.inphoto.ui.home.model

import com.linc.inphoto.ui.base.state.ItemUiState

data class StoriesUiState(
    val newStory: NewStoryUiState? = null,
    val stories: List<SingleStoryUiState> = listOf()
) : ItemUiState {
    override fun getStateItemId(): Long = javaClass.simpleName.hashCode().toLong()
}