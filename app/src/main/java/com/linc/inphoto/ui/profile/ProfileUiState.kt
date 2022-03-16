package com.linc.inphoto.ui.profile

import com.linc.inphoto.entity.User
import com.linc.inphoto.ui.base.state.UiState

data class ProfileUiState(
    val user: User? = null,
    val posts: List<ProfilePostUiState> = listOf()
) : UiState