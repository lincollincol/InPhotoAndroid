package com.linc.inphoto.ui.splash

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository
) : BaseViewModel<EmptyUiState>(navContainerHolder) {

    companion object {
        private const val SPLASH_DELAY = 1000L
    }

    override val _uiState = MutableStateFlow(EmptyUiState())

    fun checkLoggedIn() {
        viewModelScope.launch {
            try {
                val isLoggedIn = userRepository.getLoggedInUser() != null
//                delay(SPLASH_DELAY)

                if (isLoggedIn) {
                    userRepository.fetchLoggedInUser()
                    globalRouter.newRootScreen(NavScreen.MainScreen())
                } else {
                    globalRouter.newRootScreen(NavScreen.SignInScreen())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}