package com.linc.inphoto.ui.splash

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.UsersRepository
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.AppScreens
import com.linc.inphoto.utils.Constants.SPLASH_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val router: Router,
    private val usersRepository: UsersRepository
) : BaseViewModel<UiState>(router) {

    fun checkLoggedIn() = viewModelScope.launch {
        val isLoggedIn = usersRepository.hasUserData()

        delay(SPLASH_DELAY)

        val screen = when {
            isLoggedIn -> AppScreens.ProfileScreen()
            else -> AppScreens.SignInScreen()
        }
        router.newRootScreen(screen)
    }

    override val _uiState = MutableStateFlow<UiState>(EmptyUiState())
}