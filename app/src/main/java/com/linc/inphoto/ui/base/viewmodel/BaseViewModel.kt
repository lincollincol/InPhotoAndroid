package com.linc.inphoto.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<StateEntity>(
    protected val router: Router
) : ViewModel() {

    protected abstract val _uiState: MutableStateFlow<StateEntity>
    val uiState: StateFlow<StateEntity> get() = _uiState.asStateFlow()

    open fun onBackPressed() {
        router.exit()
    }

}