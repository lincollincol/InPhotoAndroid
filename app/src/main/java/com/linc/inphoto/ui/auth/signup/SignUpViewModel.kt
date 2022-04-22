package com.linc.inphoto.ui.auth.signup

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(SignUpUiState())

    fun signUp() = viewModelScope.launch {
        try {
            val state = _uiState.value
            _uiState.update { copy(isLoading = true) }
            authRepository.signUp(
                state.email.orEmpty(),
                state.username.orEmpty(),
                state.password.orEmpty(),
                state.gender,
            )
            globalRouter.newRootScreen(NavScreen.MainScreen())
        } catch (e: Exception) {
            Timber.e(e)
            _uiState.update { copy(signUpErrorMessage = e.message) }
        } finally {
            _uiState.update { copy(isLoading = false) }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { copy(email = email, signUpErrorMessage = null) }
    }

    fun updateUsername(username: String) {
        _uiState.update { copy(username = username, signUpErrorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { copy(password = password, signUpErrorMessage = null) }
    }

    fun updateRepeatPassword(password: String) {
        _uiState.update { copy(repeatPassword = password, signUpErrorMessage = null) }
    }

    fun updateGender(gender: Gender) {
        _uiState.update { copy(gender = gender) }
    }

}