package com.linc.inphoto.ui.profilefollowers

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.profilefollowers.model.FollowerUserUiState

data class ProfileFollowersUiState(
    val user: User? = null,
    val selectedPage: Int = 0,
    val searchQuery: String? = null,
    val followers: List<FollowerUserUiState> = listOf(),
    val following: List<FollowerUserUiState> = listOf()
) : UiState