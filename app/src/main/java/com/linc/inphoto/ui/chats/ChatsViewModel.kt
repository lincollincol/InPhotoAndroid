package com.linc.inphoto.ui.chats

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<ChatsUiState>(navContainerHolder) {
    override val _uiState = MutableStateFlow(ChatsUiState())
}