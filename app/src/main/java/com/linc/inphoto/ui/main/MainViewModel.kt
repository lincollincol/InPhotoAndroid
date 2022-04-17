package com.linc.inphoto.ui.main

import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<EmptyUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(EmptyUiState())

}