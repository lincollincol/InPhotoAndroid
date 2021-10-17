package com.linc.inphoto.ui.auth.sign_in

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.ui.AppScreens
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.model.auth.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseStubViewModel(router) {

    fun signIn(credentials: Credentials.SignIn) = launchCoroutine {
        val result = authRepository.signIn(
            credentials.email,
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

    fun onSignUp() {
        router.navigateTo(AppScreens.SignUpScreen())
    }

    override fun onCoroutineError(e: Exception) {

    }

}