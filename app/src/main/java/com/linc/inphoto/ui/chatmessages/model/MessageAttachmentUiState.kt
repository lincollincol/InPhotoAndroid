package com.linc.inphoto.ui.chatmessages.model

import android.net.Uri
import com.linc.inphoto.ui.base.state.UiState

data class MessageAttachmentUiState(
    val uri: Uri,
    val onRemoveClick: () -> Unit
) : UiState