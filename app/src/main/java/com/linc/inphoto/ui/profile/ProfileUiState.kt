package com.linc.inphoto.ui.profile

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.profile.model.NewPostUiState
import com.linc.inphoto.ui.profile.model.ProfilePostUiState

data class ProfileUiState(
    val username: String? = null,
    val status: String? = null,
    val avatarUrl: String? = null,
    val headerUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isLoggedInUser: Boolean = false,
    val isProfileTab: Boolean = false,
    val newPostUiState: NewPostUiState? = null,
    val posts: List<ProfilePostUiState> = listOf()
) : UiState

val ProfileUiState.isValidStatus get() = !status.isNullOrEmpty()

fun User.toUiState(
    isProfileTab: Boolean,
    newPostUiState: NewPostUiState,
    posts: List<ProfilePostUiState>
) = ProfileUiState(
    isProfileTab = isProfileTab,
    newPostUiState = newPostUiState,
    posts = posts
)