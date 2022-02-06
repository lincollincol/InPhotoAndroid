package com.linc.inphoto.ui.base.viewmodel

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.state.EmptyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EmptyViewModel @Inject constructor(router: Router) : BaseViewModel<EmptyUiState>(router) {
    override val _uiState = MutableStateFlow(EmptyUiState())
}