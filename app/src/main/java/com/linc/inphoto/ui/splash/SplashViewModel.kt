package com.linc.inphoto.ui.splash

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.AuthRepository
import com.linc.inphoto.data.repository.UsersRepository
import com.linc.inphoto.ui.AppScreens
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import com.linc.inphoto.utils.Constants.SPLASH_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router,
    private val usersRepository: UsersRepository
) : BaseStubViewModel(router) {

    fun checkLoggedIn() {
        launchCoroutine {
            val isLoggedIn = usersRepository.hasUserData()

            delay(SPLASH_DELAY)

            val screen = when {
                isLoggedIn -> AppScreens.ProfileScreen()
                else -> AppScreens.SignInScreen()
            }
            router.newRootScreen(screen)
        }
    }

    override fun onCoroutineError(e: Exception) {

    }

}