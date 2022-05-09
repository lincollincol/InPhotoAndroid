package com.linc.inphoto.ui.chats

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val chatRepository: ChatRepository
) : BaseViewModel<ChatsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ChatsUiState())

    fun loadChats() {
        viewModelScope.launch {
            try {
                chatRepository.loadUserChats()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

}