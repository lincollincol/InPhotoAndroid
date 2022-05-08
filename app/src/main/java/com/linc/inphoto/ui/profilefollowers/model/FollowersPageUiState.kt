package com.linc.inphoto.ui.profilefollowers.model

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.linc.inphoto.ui.base.state.UiState

data class FollowersPageUiState(
    val title: String,
    val screen: FragmentScreen
) : UiState