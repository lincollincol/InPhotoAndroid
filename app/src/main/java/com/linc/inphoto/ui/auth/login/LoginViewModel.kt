package com.linc.inphoto.ui.auth.login

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel<LoginUiState, LoginUiEffect>() {

    fun signIn() = launchCoroutine {
        authRepository.signIn()
    }

    fun signUp() = launchCoroutine {
        authRepository.signIn()
    }

    override fun onCoroutineError(e: Exception) {

    }

}