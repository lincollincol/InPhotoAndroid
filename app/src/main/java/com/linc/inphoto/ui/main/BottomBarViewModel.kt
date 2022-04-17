package com.linc.inphoto.ui.main

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.main.model.BottomBarUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<BottomBarUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(BottomBarUiState())

    fun showBottomBar() {
        _uiState.update { copy(visible = true) }
    }

    fun hideBottomBar() {
        _uiState.update { copy(visible = false) }
    }
}