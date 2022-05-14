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
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.mapIf
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.toMutableDeque
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatMessagesViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val messageRepository: MessageRepository
) : BaseViewModel<ChatMessagesUiState>(navContainerHolder) {

    /**
     * TODO
     * realtime updates
     * user chats
     * create chat
     * system message +
     */

    companion object {
        private const val MESSAGE_ACTION_RESULT = "message_action_result"
        private const val ATTACHMENT_SOURCE_RESULT = "attachment_source_result"
        private const val ATTACHMENT_RESULT = "attachment_result"
    }

    override val _uiState = MutableStateFlow(ChatMessagesUiState())
    private var chatId: String? = null

    fun updateMessage(message: String?) {
        _uiState.update { it.copy(message = message, isScrollDownOnUpdate = false) }
    }

    fun cancelMessageEditor() {
        _uiState.update {
            it.copy(editableMessageId = null, message = null, messageAttachments = listOf())
        }
    }

    fun loadChatMessages(chatId: String?) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                this@ChatMessagesViewModel.chatId = chatId
                messageRepository.loadChatMessagesEvents(chatId)
                    .catch { Timber.e(it) }
                    .collect { messages ->
                        val messagesStates = messages.sortedByDescending { it.createdTimestamp }
                            .map {
                                it.toUiState(
                                    onClick = { selectMessage(it.id) },
                                    onImageClick = { selectMessageFiles(it.files.map(Uri::parse)) }
                                )
                            }
                        _uiState.update { it.copy(messages = messagesStates, isLoading = false) }
                    }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            try {
                val messageId = UUID.randomUUID().toString()
                val messageText = currentState.message.orEmpty()
                val files = currentState.messageAttachments.toUriList()
                val messages = currentState.messages.toMutableDeque()
                messages.addFirst(
                    MessageUiState.getPendingMessageInstance(
                        messageId,
                        messageText,
                        files
                    )
                )
                launch { messageRepository.sendChatMessage(chatId, messageId, messageText, files) }
                _uiState.update {
                    it.copy(
                        messages = messages,
                        message = null,
                        messageAttachments = listOf(),
                        isScrollDownOnUpdate = true
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun updateMessage() {
        viewModelScope.launch {
            try {
                val editableMessageId = currentState.editableMessageId.orEmpty()
                val messageText = currentState.message.orEmpty()
                val files = currentState.messageAttachments.toUriList()
                val messages = currentState.messages.mapIf(
                    condition = { it.id == editableMessageId },
                    transform = {
                        it.copy(
                            isProcessing = true,
                            text = messageText,
                            files = files,
                            isEdited = true
                        )
                    }
                )
                launch {
                    messageRepository.updateChatMessage(
                        chatId,
                        editableMessageId,
                        messageText,
                        files
                    )
                }
                _uiState.update {
                    it.copy(
                        messages = messages,
                        editableMessageId = null,
                        message = null,
                        messageAttachments = listOf()
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
        currentState.messages.find { it.id == messageId }
            ?.let { message -> if (message.isIncoming) return }
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

    private fun selectMessageFiles(files: List<Uri>) {
        router.navigateTo(NavScreen.MediaReviewScreen(files))
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
            ?: return
        _uiState.update {
            it.copy(
                editableMessageId = messageId,
                message = message.text,
                messageAttachments = message.files.map { uri ->
                    MessageAttachmentUiState(uri) { deleteMessageAttachment(uri) }
                }
            )
        }
    }
}