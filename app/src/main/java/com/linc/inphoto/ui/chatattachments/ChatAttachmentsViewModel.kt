package com.linc.inphoto.ui.chatattachments

import com.linc.inphoto.entity.LocalMedia
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.chatattachments.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatAttachmentsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<ChatAttachmentsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ChatAttachmentsUiState())

    fun applyAttachments(attachments: List<LocalMedia>) {
        _uiState.update { state ->
            state.copy(
                attachments = attachments.map { it.toUiState() }
            )
        }
    }

    fun updateCation(caption: String) {
        _uiState.update { it.copy(captionText = caption) }
    }

    fun sendAttachmentsMessage() {

    }

    fun cancelAttachments() {
        router.closeDialog()
    }

}