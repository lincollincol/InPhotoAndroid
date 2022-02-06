package com.linc.inphoto.ui.auth.signin

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel<SignInUiState>(router) {

    override val _uiState = MutableStateFlow(SignInUiState())

    fun signIn() = viewModelScope.launch {
        try {
            val state = _uiState.value
            _uiState.update { copy(isLoading = true) }
            authRepository.signIn(
                state.login.orEmpty(),
                state.password.orEmpty()
            )
            router.newRootScreen(Navigation.ProfileScreen())
        } catch (e: Exception) {
            _uiState.update { copy(signInErrorMessage = e.message) }
            e.printStackTrace()
        } finally {
            _uiState.update { copy(isLoading = false) }
        }
    }

    fun updateLogin(login: String) {
        _uiState.update {
            copy(
                login = login,
                signInErrorMessage = null
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            copy(
                password = password,
                signInErrorMessage = null
            )
        }
    }

    fun onSignUp() {
        router.navigateTo(Navigation.Auth.SignUpScreen())
    }

}