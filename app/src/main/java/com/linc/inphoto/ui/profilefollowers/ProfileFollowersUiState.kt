package com.linc.inphoto.ui.profilefollowers

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.profilefollowers.model.FollowersPageUiState

data class ProfileFollowersUiState(
    val user: User? = null,
    val pages: List<FollowersPageUiState> = listOf()
) : UiState