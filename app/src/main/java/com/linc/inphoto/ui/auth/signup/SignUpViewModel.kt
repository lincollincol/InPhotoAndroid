package com.linc.inphoto.ui.auth.signup

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.auth.model.Credentials
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    routerHolder: NavContainerHolder,
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpUiState>(routerHolder) {

    override val _uiState = MutableStateFlow(SignUpUiState())

    fun signUp(credentials: Credentials.SignUp) = viewModelScope.launch {
        try {
            _uiState.update { copy(isLoading = true) }
            authRepository.signUp(
                credentials.email,
                credentials.username,
                credentials.password
            )
            router.newRootScreen(Navigation.ProfileScreen())
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

}