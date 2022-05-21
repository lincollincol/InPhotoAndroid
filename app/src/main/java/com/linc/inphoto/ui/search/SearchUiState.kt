package com.linc.inphoto.ui.search

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.search.model.SearchTagUiState
import com.linc.inphoto.ui.search.model.SearchUserUiState

data class SearchUiState(
    val isLoading: Boolean = false,
    val selectedPage: Int = 0,
    val searchQuery: String? = null,
    val users: List<SearchUserUiState> = listOf(),
    val tags: List<SearchTagUiState> = listOf()
) : UiState