package com.linc.inphoto.ui.chatmessages

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MessageRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.chatmessages.model.*
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.mapIf
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

    /**
     * TODO
     * isUpdated state
     * send media (attachments)
     * update media
     * realtime updates
     */

    companion object {
        private const val MESSAGE_ACTION_RESULT = "message_action_result"
        private const val ATTACHMENT_SOURCE_RESULT = "attachment_source_result"
        private const val ATTACHMENT_RESULT = "attachment_result"
    }

    override val _uiState = MutableStateFlow(ChatMessagesUiState())
    private var chatId: String? = null

    fun updateMessage(message: String?) {
        _uiState.update { it.copy(message = message) }
    }

    fun cancelMessageEditor() {
        _uiState.update { it.copy(editableMessageId = null) }
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

    fun updateMessage() {
        viewModelScope.launch {
            try {
                val messages = currentState.messages.mapIf(
                    predicate = { it.id == currentState.editableMessageId },
                    transform = { it.copy(isPending = true, text = currentState.message.orEmpty()) }
                )
                _uiState.update { it.copy(messages = messages) }
                messageRepository.updateChatMessage(
                    chatId,
                    currentState.editableMessageId.orEmpty(),
                    currentState.message.orEmpty(),
                    listOf()
                )
                _uiState.update { state ->
                    state.copy(
                        messages = messages.mapIf(
                            predicate = { it.id == currentState.editableMessageId },
                            transform = { it.copy(isPending = false) }
                        ),
                        message = null,
                        editableMessageId = null,
                        isScrollDownOnUpdate = false
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun selectAttachments() {
        router.setResultListener(ATTACHMENT_SOURCE_RESULT) { result ->
            val source = result.safeCast<AttachmentSource>() ?: return@setResultListener
            val screen = when (source) {
                AttachmentSource.Gallery ->
                    NavScreen.GalleryScreen(GalleryIntent.Result(ATTACHMENT_RESULT))
                AttachmentSource.Camera ->
                    NavScreen.CameraScreen(CameraIntent.Result(ATTACHMENT_RESULT))
            }
            router.navigateTo(screen)
        }
        router.setResultListener(ATTACHMENT_RESULT) { result ->
            val uri = result.safeCast<Uri>() ?: return@setResultListener
            val attachments = currentState.messageAttachments.toMutableDeque()
            if (attachments.firstOrNull { it.uri == uri } != null) {
                return@setResultListener
            }
            val attachmentUiState = MessageAttachmentUiState(uri) { deleteMessageAttachment(uri) }
            attachments.addFirst(attachmentUiState)
            _uiState.update { it.copy(messageAttachments = attachments) }
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            ATTACHMENT_SOURCE_RESULT,
            AttachmentSource.getAvailableSources()
        )
        router.showDialog(pickerScreen)
    }

    private fun selectMessage(messageId: String) {
        router.setResultListener(MESSAGE_ACTION_RESULT) { result ->
            val operation = result.safeCast<MessageOperation>() ?: return@setResultListener
            when (operation) {
                MessageOperation.Edit -> editMessage(messageId)
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

    private fun deleteMessageAttachment(uri: Uri) {
        val attachments = currentState.messageAttachments.filter { it.uri != uri }
        _uiState.update { it.copy(messageAttachments = attachments) }
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

    private fun editMessage(messageId: String) {
        val message = currentState.messages.find { it.id == messageId }
        _uiState.update {
            it.copy(editableMessageId = messageId, message = message?.text)
        }
    }
}