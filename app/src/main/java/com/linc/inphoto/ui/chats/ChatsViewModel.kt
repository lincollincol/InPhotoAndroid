package com.linc.inphoto.ui.chats

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chats.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
                _uiState.update { it.copy(isLoading = true) }
                val chats = chatRepository.loadUserChats().map {
                    it.toUiState { selectChat(it.id) }
                }
                _uiState.update { it.copy(chats = chats) }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun selectChat(chatId: String) {

    }
}