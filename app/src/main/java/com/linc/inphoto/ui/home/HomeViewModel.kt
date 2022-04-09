package com.linc.inphoto.ui.home

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<HomeUiState>(navContainerHolder) {
    override val _uiState = MutableStateFlow(HomeUiState())
}