package com.linc.inphoto.ui.profile

import com.linc.inphoto.ui.base.UiState
import com.linc.inphoto.ui.common.model.user.UserModel

sealed class ProfileUiState : UiState {
    data class UpdateUserData(val userModel: UserModel) : ProfileUiState()
}