package com.linc.inphoto.ui.splash

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.AppScreens
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router
) : BaseStubViewModel() {

    fun checkLoggedIn() {
        router.newRootScreen(AppScreens.LoginScreen())
    }

    override fun onCoroutineError(e: Exception) {

    }

}