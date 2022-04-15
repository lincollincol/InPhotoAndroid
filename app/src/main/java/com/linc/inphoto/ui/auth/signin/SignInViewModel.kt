package com.linc.inphoto.ui.auth.signin

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.AuthRepository
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
class SignInViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val authRepository: AuthRepository
) : BaseViewModel<SignInUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(SignInUiState())

    fun signIn() = viewModelScope.launch {
        try {
            val state = _uiState.value
            _uiState.update { copy(isLoading = true) }
            authRepository.signIn(
                state.login.orEmpty(),
                state.password.orEmpty()
            )
            globalRouter.newRootScreen(NavScreen.MainScreen())
        } catch (e: Exception) {
            Timber.e(e)
            _uiState.update { copy(signInErrorMessage = e.message) }
        } finally {
            _uiState.update { copy(isLoading = false) }
        }
    }

    fun updateLogin(login: String) {
        _uiState.update { copy(login = login, signInErrorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { copy(password = password, signInErrorMessage = null) }
    }

    fun signUp() {
        globalRouter.navigateTo(NavScreen.SignUpScreen())
    }

}