package com.linc.inphoto.ui.splash

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router
) : ViewModel() {

    fun onEvent(event: SplashEvent) {
        if(event is SplashEvent.CheckLoggedInEvent) {
            checkLoggedIn()
        }
    }

    private fun checkLoggedIn() {
        router.newRootScreen(AppScreens.LoginScreen())
    }

}