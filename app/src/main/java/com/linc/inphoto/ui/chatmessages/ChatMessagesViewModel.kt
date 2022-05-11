package com.linc.inphoto.ui.chatmessages

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MessageRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chatmessages.model.MessageOperation
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.ui.chatmessages.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.toMutableDeque
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

    companion object {
        private const val MESSAGE_ACTION_RESULT = "message_action_result"
    }

    override val _uiState = MutableStateFlow(ChatMessagesUiState())
    private var chatId: String? = null

    fun updateMessage(message: String?) {
        _uiState.update { it.copy(message = message) }
    }

    fun loadChatMessages(chatId: String?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                this@ChatMessagesViewModel.chatId = chatId
                val messages = messageRepository.loadChatMessages(chatId)
                    .sortedByDescending { it.createdTimestamp }
                    .map {
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

    fun sendMessage() {
        viewModelScope.launch {
            try {
                showPendingMessage()
                sendPendingMessage()
                _uiState.update { it.copy(message = null) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private suspend fun showPendingMessage() {
        val messages = currentState.messages.toMutableDeque()
        val localMessage = MessageUiState(currentState.message.orEmpty(), listOf())
        messages.addFirst(localMessage)
        _uiState.update { it.copy(messages = messages, isScrollDownOnUpdate = true) }
    }

    private suspend fun sendPendingMessage() {
        val messages = currentState.messages.toMutableDeque()
        val message = messageRepository.sendChatMessage(
            chatId,
            currentState.message.orEmpty(),
            listOf()
        )?.let {
            it.toUiState(
                onClick = { selectMessage(it.id) },
                onImageClick = { selectMessageFiles(it.files) }
            )
        } ?: return
        messages.removeFirst()
        messages.addFirst(message)
        _uiState.update { it.copy(messages = messages, isScrollDownOnUpdate = false) }
    }

    private fun selectMessage(messageId: String) {
        router.setResultListener(MESSAGE_ACTION_RESULT) { result ->
            val operation = result.safeCast<MessageOperation>() ?: return@setResultListener
            when (operation) {
                MessageOperation.Edit -> deleteMessage(messageId)
                MessageOperation.Delete -> deleteMessage(messageId)
            }
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            MESSAGE_ACTION_RESULT, MessageOperation.getMessageOperations()
        )
        router.showDialog(pickerScreen)
    }

    private fun selectMessageFiles(files: List<String>) {
        Timber.d(files.toString())
    }

    private fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            try {
                val messages = currentState.messages.filter { it.id != messageId }
                messageRepository.deleteChatMessage(chatId, messageId)
                _uiState.update { it.copy(messages = messages) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}