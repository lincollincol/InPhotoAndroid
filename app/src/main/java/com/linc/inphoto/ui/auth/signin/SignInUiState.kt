package com.linc.inphoto.ui.auth.signin

import com.linc.inphoto.ui.base.state.UiState

data class SignInUiState(
    val login: String? = null,
    val password: String? = null
) : UiState

val SignInUiState.signInEnabled: Boolean get() = !login.isNullOrEmpty() && !password.isNullOrEmpty()