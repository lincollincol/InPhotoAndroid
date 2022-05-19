package com.linc.inphoto.ui.search.model

import com.linc.inphoto.entity.post.Tag
import com.linc.inphoto.ui.base.state.ItemUiState

data class SearchTagUiState(
    val tagId: String,
    val tag: String,
    val onClick: () -> Unit
) : ItemUiState {
    override fun getStateItemId(): Long = tagId.hashCode().toLong()
}

fun Tag.toUiState(onClick: () -> Unit) = SearchTagUiState(
    tagId = id,
    tag = name,
    onClick = onClick
)