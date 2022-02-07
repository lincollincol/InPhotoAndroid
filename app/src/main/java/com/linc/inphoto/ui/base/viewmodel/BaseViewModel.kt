package com.linc.inphoto.ui.base.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.navigation.Navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<StateEntity>(
    protected val router: Router
) : ViewModel() {

    protected abstract val _uiState: MutableStateFlow<StateEntity>
    val uiState: StateFlow<StateEntity> get() = _uiState.asStateFlow()

    protected fun showInfo(@StringRes title: Int, @StringRes description: Int) {
        router.navigateTo(Navigation.Common.InfoMessageScreen(title, description))
    }

    open fun onBackPressed() {
        router.exit()
    }

}