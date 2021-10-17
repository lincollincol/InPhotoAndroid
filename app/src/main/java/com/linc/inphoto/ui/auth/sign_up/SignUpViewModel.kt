package com.linc.inphoto.ui.auth.sign_up

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.auth.sign_in.SignInUiState
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.model.auth.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpUiState, BaseUiEffect>(router) {

    fun signUp(credentials: Credentials.SignUp) = launchCoroutine {
        authRepository.signUp(
            credentials.email,
            credentials.username,
            credentials.password
        )
    }

    override fun onCoroutineError(e: Exception) {

    }

}