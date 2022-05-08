package com.linc.inphoto.ui.main

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.main.model.BottomBarUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<BottomBarUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(BottomBarUiState())

    fun showBottomBar(visible: Boolean = true) {
        _uiState.update { it.copy(visible = visible) }
    }

    fun hideBottomBar() {
        _uiState.update { it.copy(visible = false) }
    }
}