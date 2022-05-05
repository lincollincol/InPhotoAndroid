package com.linc.inphoto.ui.profile

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.profile.model.NewPostUiState
import com.linc.inphoto.ui.profile.model.ProfilePostUiState

data class ProfileUiState(
    val user: User? = null,
    val isProfileTab: Boolean = false,
    val newPostUiState: NewPostUiState? = null,
    val posts: List<ProfilePostUiState> = listOf()
) : UiState

val ProfileUiState.isStatusValid get() = !user?.status.isNullOrEmpty()