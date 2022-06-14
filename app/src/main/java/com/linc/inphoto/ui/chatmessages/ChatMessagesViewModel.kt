package com.linc.inphoto.ui.chatmessages

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.data.repository.MessageRepository
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.chatmessages.model.*
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.ResourceProvider
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.mapIf
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.toMutableDeque
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatMessagesViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<ChatMessagesUiState>(navContainerHolder) {

    companion object {
        private const val MESSAGE_ACTION_RESULT = "message_action_result"
        private const val ATTACHMENT_SOURCE_RESULT = "attachment_source_result"
        private const val ATTACHMENT_RESULT = "attachment_result"
    }

    override val _uiState = MutableStateFlow(ChatMessagesUiState())
    private var chatId: String? = null
    private var receiverId: String? = null

    fun updateMessage(message: String?) {
        _uiState.update { it.copy(message = message) }
    }

    fun cancelMessageEditor() {
        _uiState.update {
            it.copy(editableMessageId = null, message = null, messageAttachments = listOf())
        }
    }

    fun selectUserProfile() {
        router.navigateTo(NavScreen.ProfileScreen(receiverId))
    }

    fun loadConversation(conversation: ConversationParams) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        username = conversation.username,
                        userAvatarUrl = conversation.avatarUrl
                    )
                }
                receiverId = conversation.userId
                chatId = when (conversation) {
                    is ConversationParams.Existent -> conversation.chatId
                    is ConversationParams.Undefined ->
                        chatRepository.findChatWithUser(conversation.userId)?.id
                    else -> null
                }
                if (!chatId.isNullOrEmpty()) {
                    loadChatMessages(chatId.orEmpty())
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadChatMessages(chatId: String) = coroutineScope {
        messageRepository.getChatMessages(chatId)
            .catch { Timber.e(it) }
            .distinctUntilChanged()
            .collect { messages ->
                val messagesStates = messages.sortedByDescending { it.createdTimestamp }
                    .map {
                        it.toUiState(
                            onClick = { selectMessage(it) },
                            onImageClick = { selectMessageFiles(it.files.map(Uri::parse)) }
                        )
                    }
                _uiState.update {
                    it.copy(
                        messages = messagesStates,
                        isLoading = false,
                        isScrollDownOnUpdate = false
                    )
                }
            }
    }

    fun sendMessage() {
        viewModelScope.launch {
            try {
                if (chatId.isNullOrEmpty()) {
                    chatId = chatRepository.createChat(receiverId)
                    launch { loadChatMessages(chatId.orEmpty()) }
                }
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
                _uiState.update {
                    it.copy(
                        messages = messages,
                        message = null,
                        messageAttachments = listOf(),
                        isScrollDownOnUpdate = true
                    )
                }
                launch { messageRepository.sendChatMessage(chatId, messageId, messageText, files) }
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
            resourceProvider.getString(R.string.choose_attachment_source),
            AttachmentSource.getAvailableSources()
        )
        router.showDialog(pickerScreen)
    }

    private fun selectMessage(message: Message) {
        if (message.isIncoming) return
        router.setResultListener(MESSAGE_ACTION_RESULT) { result ->
            val operation = result.safeCast<MessageOperation>() ?: return@setResultListener
            when (operation) {
                MessageOperation.Edit -> editMessage(message.id)
                MessageOperation.Delete -> deleteMessage(message.id)
            }
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            MESSAGE_ACTION_RESULT,
            resourceProvider.getString(R.string.choose_message_action),
            MessageOperation.getMessageOperations()
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
        val message = currentState.messages.find { it.id == messageId } ?: return
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