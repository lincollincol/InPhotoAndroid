package com.linc.inphoto.ui.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.navigation.AppRouter
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<StateEntity : UiState>(
    private val navContainerHolder: NavContainerHolder
) : ViewModel() {

    protected var containerId: String? = null
    protected val globalRouter: AppRouter get() = navContainerHolder.getGlobalRouter()
    protected val router: AppRouter get() = navContainerHolder.getRouter(containerId.orEmpty())

    protected val currentState: StateEntity get() = _uiState.value
    protected abstract val _uiState: MutableStateFlow<StateEntity>
    val uiState: StateFlow<StateEntity> get() = _uiState.asStateFlow()

    protected fun showInfo(@StringRes title: Int, @StringRes description: Int) {
        router.navigateTo(NavScreen.InfoMessageScreen(title, description))
    }

    fun setupContainerId(containerId: String?) {
        this.containerId = containerId
    }

    open fun onBackPressed() {
        router.exit()
    }

}