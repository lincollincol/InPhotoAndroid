package com.linc.inphoto.ui.splash

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.UsersRepository
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router,
    private val usersRepository: UsersRepository
) : BaseViewModel<UiState>(router) {

    companion object {
        private const val SPLASH_DELAY = 1000L
    }

    fun checkLoggedIn() {
        viewModelScope.launch {
            try {
                val isLoggedIn = usersRepository.getLoggedInUser() != null

                delay(SPLASH_DELAY)

                val screen = when {
                    isLoggedIn -> Navigation.ProfileScreen()
                    else -> Navigation.Auth.SignInScreen()
                }

                router.newRootScreen(screen)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override val _uiState = MutableStateFlow<UiState>(EmptyUiState())
}