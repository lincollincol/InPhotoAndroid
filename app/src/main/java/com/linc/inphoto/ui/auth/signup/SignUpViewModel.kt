package com.linc.inphoto.ui.auth.signup

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.navigation.AppScreens
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.auth.model.Credentials
import com.linc.inphoto.ui.base.UiEffect
import com.linc.inphoto.ui.base.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel<UiState, UiEffect>(router) {

    fun signUp(credentials: Credentials.SignUp) = launchCoroutine {
        val result = authRepository.signUp(
            credentials.email,
            credentials.username,
            credentials.password
        )
        result.fold(
            onSuccess = {
                router.newRootScreen(AppScreens.ProfileScreen())
            },
            onFailure = {
                setEffect(BaseUiEffect.Error(it.message!!))
            }
        )
    }

    override fun onCoroutineError(e: Exception) {

    }

}