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
     * show files
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
                val messageText = currentState.message.orEmpty()
                val files = currentState.messageAttachments.toUriList()
                launch { showPendingMessage(messageText, files) }
                launch { sendPendingMessage(messageText, files) }
                _uiState.update { it.copy(message = null, messageAttachments = listOf()) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private suspend fun showPendingMessage(messageText: String, files: List<Uri>) {
        val messages = currentState.messages.toMutableDeque()
        messages.addFirst(MessageUiState(messageText, files))
        _uiState.update { it.copy(messages = messages, isScrollDownOnUpdate = true) }
    }

    private suspend fun sendPendingMessage(messageText: String, files: List<Uri>) {
        val messages = currentState.messages.toMutableDeque()
        val message = messageRepository.sendChatMessage(chatId, messageText, files)?.let {
            it.toUiState(
                onClick = { selectMessage(it.id) },
                onImageClick = { selectMessageFiles(it.files) }
            )
        } ?: return
        messages.removeFirst()
        messages.addFirst(message)
        _uiState.update { it.copy(messages = messages, isScrollDownOnUpdate = true) }
    }

    fun updateMessage() {
        viewModelScope.launch {
            try {
                val editableMessageId = currentState.editableMessageId.orEmpty()
                val messageText = currentState.message.orEmpty()
                val files = currentState.messageAttachments.toUriList()
                launch { showUpdatingEditableMessage(editableMessageId, messageText, files) }
                launch { updateEditableMessage(editableMessageId, messageText, files) }
                _uiState.update {
                    it.copy(editableMessageId = null, message = null, messageAttachments = listOf())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun showUpdatingEditableMessage(
        editableMessageId: String,
        messageText: String, files:
        List<Uri>
    ) {
        val messages = currentState.messages.mapIf(
            condition = { it.id == editableMessageId },
            transform = {
                it.copy(isProcessing = true, text = messageText, files = files, isEdited = true)
            }
        )
        _uiState.update { it.copy(messages = messages) }
    }

    private suspend fun updateEditableMessage(
        editableMessageId: String,
        messageText: String,
        files: List<Uri>
    ) {
        messageRepository.updateChatMessage(chatId, editableMessageId, messageText, files)
        _uiState.update { state ->
            state.copy(
                messages = currentState.messages.mapIf(
                    condition = { it.id == editableMessageId },
                    transform = { it.copy(isProcessing = false) }
                ),
                isScrollDownOnUpdate = false
            )
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