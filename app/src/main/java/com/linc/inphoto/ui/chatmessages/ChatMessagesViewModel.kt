package com.linc.inphoto.ui.chatmessages

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MessageRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chatmessages.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatMessagesViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val messageRepository: MessageRepository
) : BaseViewModel<ChatMessagesUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ChatMessagesUiState())

    fun loadChatMessages(chatId: String?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val messages = messageRepository.loadChatMessages(chatId).map {
                    it.toUiState(
                        onClick = { selectMessage(it.id) },
                        onImageClick = { selectMessageFiles(it.files) }
                    )
                }
                _uiState.update { it.copy(messages = messages) }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun selectMessage(messageId: String) {
        Timber.d(messageId)
    }

    private fun selectMessageFiles(files: List<String>) {
        Timber.d(files.toString())
    }
}