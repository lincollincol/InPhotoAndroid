package com.linc.inphoto.ui.auth.signup

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.auth.model.Credentials
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpUiState>(router) {

    override val _uiState = MutableStateFlow(SignUpUiState())

    fun signUp(credentials: Credentials.SignUp) = viewModelScope.launch {
        try {
            val result = authRepository.signUp(
                credentials.email,
                credentials.username,
                credentials.password
            )

            router.newRootScreen(AppScreens.ProfileScreen())
        } catch (e: Exception) {

        }
    }

}