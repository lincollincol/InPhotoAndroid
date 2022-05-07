package com.linc.inphoto.ui.followerslist

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.followerslist.model.FollowerUserUiState

data class FollowersListUiState(
    val users: List<FollowerUserUiState> = listOf()
) : UiState