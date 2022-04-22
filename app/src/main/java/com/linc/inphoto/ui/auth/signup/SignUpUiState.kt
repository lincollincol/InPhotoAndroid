package com.linc.inphoto.ui.auth.signup

import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.ui.base.state.UiState

data class SignUpUiState(
    val signUpErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val email: String? = null,
    val username: String? = null,
    val password: String? = null,
    val gender: Gender = Gender.MALE,
    val repeatPassword: String? = null
) : UiState

val SignUpUiState.passwordValid: Boolean
    get() =
        !password.isNullOrEmpty() && password == repeatPassword

val SignUpUiState.loginValid: Boolean
    get() =
        !email.isNullOrEmpty() && !username.isNullOrEmpty()

val SignUpUiState.signUpEnabled: Boolean get() = loginValid && passwordValid