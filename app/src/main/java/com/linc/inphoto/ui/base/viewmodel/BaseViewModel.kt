package com.linc.inphoto.ui.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.linc.inphoto.ui.navigation.AppRouter
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<StateEntity>(
    private val routerHolder: NavContainerHolder
) : ViewModel() {

    private var tabId: String? = null
    protected val router: AppRouter get() = routerHolder.getRouter(tabId.orEmpty())

    protected abstract val _uiState: MutableStateFlow<StateEntity>
    val uiState: StateFlow<StateEntity> get() = _uiState.asStateFlow()

    protected fun showInfo(@StringRes title: Int, @StringRes description: Int) {
        router.navigateTo(Navigation.InfoMessageScreen(title, description))
    }

    fun setupTabId(tabId: String?) {
        this.tabId = tabId
    }

    open fun onBackPressed() {
        router.exit()
    }

}