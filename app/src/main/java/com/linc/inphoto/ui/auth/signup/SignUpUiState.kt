package com.linc.inphoto.ui.auth.signup

import com.linc.inphoto.ui.base.state.UiState

data class SignUpUiState(
    val login: String? = null,
    val password: String? = null,
    val repeatPassword: String? = null
) : UiState

val SignUpUiState.passwordsAreSame: Boolean
    get() {
        return !password.isNullOrEmpty() && password == repeatPassword
    }

val SignUpUiState.signUpEnabled: Boolean
    get() {
        return !login.isNullOrEmpty() && passwordsAreSame
    }