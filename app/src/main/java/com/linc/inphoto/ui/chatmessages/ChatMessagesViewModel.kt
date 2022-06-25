package com.linc.inphoto.ui.chatmessages

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.android.AudioPlaybackManager
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.data.repository.MessageRepository
import com.linc.inphoto.entity.chat.Message
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.chatmessages.model.*
import com.linc.inphoto.ui.filemanager.model.FileManagerIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.*
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
    private val audioPlaybackManager: AudioPlaybackManager
) : BaseViewModel<ChatMessagesUiState>(navContainerHolder) {

    companion object {
        private const val MESSAGE_ACTION_RESULT = "message_action_result"
        private const val ATTACHMENT_SOURCE_RESULT = "attachment_source_result"
        private const val ATTACHMENT_FILE_RESULT = "attachment_file_result"
        private const val ATTACHMENT_SEND_STATE_RESULT = "attachment_send_state_result"
    }

    override val _uiState = MutableStateFlow(ChatMessagesUiState())
    private var chatId: String? = null
    private var receiverId: String? = null

    init {
        audioPlaybackManager.initializeController()
    }

    override fun onBackPressed() {
        audioPlaybackManager.clearAudio()
        super.onBackPressed()
    }

    override fun onCleared() {
        audioPlaybackManager.releaseController()
        super.onCleared()
    }

    fun updateMessage(message: String?) {
        _uiState.update { it.copy(message = message) }
    }

    fun cancelMessageEditor() {
        _uiState.update {
            it.copy(
                editableMessageId = null,
                message = null,
                messageAttachments = listOf()
            )
        }
    }

    fun selectUserProfile() {
        router.navigateTo(NavScreen.ProfileScreen(receiverId))
    }

    fun messagesScrolledDown() {
//        _uiState.update { it.copy(isScrollDownOnUpdate = false) }
    }

    fun keyboardHidden() {
        _uiState.update { it.copy(isHideKeyboard = false) }
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
                            onImageClick = { handleMessageImage(it) },
                            onAudioClick = { handleMessageAudio(it) },
                            onVideoClick = { handleMessageImage(it) },
                        )
                    }
                _uiState.update {
                    it.copy(
                        messages = messagesStates,
                        isLoading = false,
                        isScrollDownOnUpdate = true
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
                val messages = currentState.messages.toMutableDeque()
                messages.addFirst(
                    MessageUiState.getPendingMessageInstance(
                        messageId,
                        messageText,
                        null
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
                launch {
                    messageRepository.sendChatMessage(
                        chatId,
                        messageId,
                        messageText,
                        listOf()
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
                val messages = currentState.messages.mapIf(
                    condition = { it.id == editableMessageId },
                    transform = {
                        it.copy(
                            isProcessing = true,
                            text = messageText,
                            isEdited = true
                        )
                    }
                )
                launch {
                    messageRepository.updateChatMessage(
                        chatId,
                        editableMessageId,
                        messageText
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

    private fun openAttachmentSource(source: AttachmentSource?) {
        val screen = when (source) {
            AttachmentSource.Gallery ->
                NavScreen.GalleryScreen(GalleryIntent.Result(ATTACHMENT_FILE_RESULT))
            AttachmentSource.Camera ->
                NavScreen.CameraScreen(CameraIntent.Result(ATTACHMENT_FILE_RESULT))
            AttachmentSource.Audio ->
                NavScreen.AudioLibraryScreen(
                    MIME_AUDIO_PREFIX,
                    FileManagerIntent.SingleResult(ATTACHMENT_FILE_RESULT)
                )
            AttachmentSource.Video ->
                NavScreen.AudioLibraryScreen(
                    MIME_VIDEO_PREFIX,
                    FileManagerIntent.SingleResult(ATTACHMENT_FILE_RESULT)
                )
            AttachmentSource.Document ->
                NavScreen.AudioLibraryScreen(
                    MIME_APPLICATION_PREFIX,
                    FileManagerIntent.SingleResult(ATTACHMENT_FILE_RESULT)
                )
            else -> return
        }
        router.setResultListener(ATTACHMENT_FILE_RESULT) { result ->
            val uri = result.safeCast<LocalMedia>() ?: return@setResultListener
            handleSelectedAttachments(uri)
        }
        router.navigateTo(screen)
    }

    private fun handleSelectedAttachments(attachment: LocalMedia) {
        val screen = NavScreen.MessageAttachmentsScreen(
            ATTACHMENT_SEND_STATE_RESULT,
            chatId,
            receiverId,
            listOf(attachment)
        )
        router.setResultListener(ATTACHMENT_SEND_STATE_RESULT) {
            _uiState.update { it.copy(isScrollDownOnUpdate = true) }
        }
        router.showDialog(screen)
    }

    fun selectAttachments() {
        router.setResultListener(ATTACHMENT_SOURCE_RESULT) { result ->
            openAttachmentSource(result.safeCast<AttachmentSource>())
        }
        router.showDialog(
            NavScreen.ChooseOptionScreen(
                ATTACHMENT_SOURCE_RESULT,
                AttachmentSource.getAvailableSources()
            )
        )
    }

    private fun selectMessage(message: Message) {
        if (message.isIncoming) return
        _uiState.update { it.copy(isHideKeyboard = true) }
        router.setResultListener(MESSAGE_ACTION_RESULT) { result ->
            val operation = result.safeCast<MessageOperation>() ?: return@setResultListener
            when (operation) {
                MessageOperation.Edit -> editMessage(message.id)
                MessageOperation.Delete -> deleteMessage(message.id)
            }
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            MESSAGE_ACTION_RESULT,
            MessageOperation.getMessageOperations()
        )
        router.showDialog(pickerScreen)
    }

    private fun handleMessageAudio(message: Message) {
        if (message.attachments.isEmpty()) {
            return
        }
        val messageUiState = currentState.messages
            .firstOrNull { it.id == message.id }
            ?: return
        audioPlaybackManager.setAudio(message.attachments.first())
        when {
            messageUiState.isAudioPlaying -> audioPlaybackManager.pause()
            else -> audioPlaybackManager.play()
        }
        val messages = currentState.messages
            .map { it.copy(isAudioPlaying = false) }
            .mapIf(
                condition = { it.id == message.id },
                transform = { it.copy(isAudioPlaying = !messageUiState.isAudioPlaying) }
            )
        _uiState.update { it.copy(messages = messages) }
    }

    private fun handleMessageImage(message: Message) {
        if (message.attachments.isEmpty()) {
            return
        }
        val images = message.attachments.map { it.uri }
        router.navigateTo(NavScreen.MediaReviewScreen(images))
    }

    private fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            try {
                val messages = currentState.messages.filter { it.id != messageId }
                messageRepository.deleteChatMessage(chatId, messageId)
                _uiState.update { it.copy(messages = messages, isScrollDownOnUpdate = false) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun editMessage(messageId: String) {
        val message = currentState.messages.find { it.id == messageId } ?: return
        _uiState.update {
            it.copy(editableMessageId = messageId, message = message.text)
        }
    }
}