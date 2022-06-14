package com.linc.inphoto.ui.auth.signup

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
            _uiState.update { it.copy(isLoading = true) }
            authRepository.signUp(
                currentState.email.orEmpty(),
                currentState.username.orEmpty(),
                currentState.password.orEmpty(),
                currentState.gender,
            )
            globalRouter.newRootScreen(NavScreen.MainScreen())
        } catch (e: Exception) {
            Timber.e(e)
            _uiState.update { it.copy(signUpErrorMessage = e.message) }
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, signUpErrorMessage = null) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username, signUpErrorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, signUpErrorMessage = null) }
    }

    fun updateRepeatPassword(password: String) {
        _uiState.update { it.copy(repeatPassword = password, signUpErrorMessage = null) }
    }

    fun updateGender(gender: Gender) {
        _uiState.update { it.copy(gender = gender) }
    }

    override fun onBackPressed() {
        globalRouter.exit()
    }
}