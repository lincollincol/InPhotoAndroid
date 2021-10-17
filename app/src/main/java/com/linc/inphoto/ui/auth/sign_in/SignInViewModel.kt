package com.linc.inphoto.ui.auth.sign_in

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.AppScreens
import com.linc.inphoto.ui.auth.sign_in.SignInUiState
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.model.auth.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel<SignInUiState, BaseUiEffect>(router) {

    fun signIn(credentials: Credentials.SignIn) = launchCoroutine {
        authRepository.signIn(
            credentials.email,
            credentials.password
        )
    }

    fun onSignUp() {
        router.navigateTo(AppScreens.SignUpScreen())
    }

    override fun onCoroutineError(e: Exception) {

    }

}