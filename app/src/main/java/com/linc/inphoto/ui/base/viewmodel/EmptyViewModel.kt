package com.linc.inphoto.ui.base.viewmodel

import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EmptyViewModel @Inject constructor(routerHolder: NavContainerHolder) :
    BaseViewModel<EmptyUiState>(routerHolder) {
    override val _uiState = MutableStateFlow(EmptyUiState())
}