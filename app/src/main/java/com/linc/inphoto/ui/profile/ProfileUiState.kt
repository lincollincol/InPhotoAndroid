package com.linc.inphoto.ui.profile

import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.profile.model.NewPostUiState
import com.linc.inphoto.ui.profile.model.ProfilePostUiState

/*data class ProfileUiState(
    val userId: String? = null,
    val username: String? = null,
    val status: String? = null,
    val avatarUrl: String? = null,
    val headerUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowingUser: Boolean = false,
    val isLoggedInUser: Boolean = true,
    val isProfileTab: Boolean = false,
    val newPostUiState: NewPostUiState? = null,
    val posts: List<ProfilePostUiState> = listOf()
) : UiState*/
data class ProfileUiState(
    val user: User? = null,
    val isProfileTab: Boolean = false,
    val newPostUiState: NewPostUiState? = null,
    val posts: List<ProfilePostUiState> = listOf()
) : UiState

val ProfileUiState.isStatusValid get() = !user?.status.isNullOrEmpty()
//val ProfileUiState.isValidStatus get() = !status.isNullOrEmpty()