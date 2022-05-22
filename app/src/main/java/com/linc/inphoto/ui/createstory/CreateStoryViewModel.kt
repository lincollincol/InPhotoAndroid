package com.linc.inphoto.ui.createstory

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class CreateStoryViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<CreateStoryUiState>(navContainerHolder) {
    override val _uiState = MutableStateFlow(CreateStoryUiState())
}