package com.linc.inphoto.ui.chatattachments

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.ChatRepository
import com.linc.inphoto.data.repository.MessageRepository
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chatattachments.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatAttachmentsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository
) : BaseViewModel<ChatAttachmentsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ChatAttachmentsUiState())
    private var chatId: String? = null
    private var receiverId: String? = null

    fun applyAttachments(
        chatId: String?,
        receiverId: String?,
        attachments: List<LocalMedia>
    ) {
        this.receiverId = receiverId
        this.chatId = chatId
        _uiState.update { state ->
            state.copy(attachments = attachments.map { it.toUiState() })
        }
    }

    fun updateCation(caption: String) {
        _uiState.update { it.copy(captionText = caption) }
    }

    fun sendAttachmentsMessage() {
        viewModelScope.launch {
            try {
                if (chatId.isNullOrEmpty()) {
                    chatId = chatRepository.createChat(receiverId)
                }
                val messageId = UUID.randomUUID().toString()
                val messageText = currentState.captionText.orEmpty()
                val attachments = currentState.attachments.map { it.localMedia }
                messageRepository.sendChatMessage(
                    chatId,
                    messageId,
                    messageText,
                    attachments
                )
                router.closeDialog()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelAttachments() {
        router.closeDialog()
    }

}