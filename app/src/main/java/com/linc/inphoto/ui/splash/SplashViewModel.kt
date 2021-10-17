package com.linc.inphoto.ui.splash

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.AppScreens
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import com.linc.inphoto.utils.Constants.SPLASH_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router
) : BaseStubViewModel(router) {

    fun checkLoggedIn() {
        launchCoroutine {
            delay(SPLASH_DELAY)
            router.newRootScreen(AppScreens.SignInScreen())
        }
    }

    override fun onCoroutineError(e: Exception) {

    }

}