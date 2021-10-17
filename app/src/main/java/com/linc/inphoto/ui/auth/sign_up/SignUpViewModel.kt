package com.linc.inphoto.ui.auth.sign_up

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
class SignUpViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseStubViewModel(router) {

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