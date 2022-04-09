package com.linc.inphoto.ui.splash

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository
) : BaseViewModel<UiState>(navContainerHolder) {

    companion object {
        private const val SPLASH_DELAY = 1000L
    }

    fun checkLoggedIn() {
        viewModelScope.launch {
            try {
                val isLoggedIn = userRepository.getLoggedInUser() != null

                delay(SPLASH_DELAY)

                val screen = when {
                    isLoggedIn -> NavScreen.ProfileScreen()
                    else -> NavScreen.SignInScreen()
                }

                router.newRootScreen(screen)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override val _uiState = MutableStateFlow<UiState>(EmptyUiState())
}