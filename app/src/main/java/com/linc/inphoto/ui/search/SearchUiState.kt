package com.linc.inphoto.ui.search

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.search.model.SearchUserUiState

data class SearchUiState(
    val users: List<SearchUserUiState> = listOf()
) : UiState