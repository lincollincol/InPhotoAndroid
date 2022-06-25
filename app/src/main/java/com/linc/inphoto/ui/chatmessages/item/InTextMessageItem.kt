package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMessageIncomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem

class InTextMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemMessageIncomingBinding>(messageUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemMessageIncomingBinding, position: Int) {
        with(viewBinding) {
            messageTextView.apply {
                text = messageUiState.text
                show(messageUiState.text.isNotEmpty())
            }
            timeTextView.text = DateFormatter.format(
                messageUiState.createdTimestamp,
                TIME_PATTERN_SEMICOLON
            )
            root.setOnThrottledClickListener { messageUiState.onClick() }
            processingProgressBar.show(messageUiState.isProcessing)
            editedTextView.show(messageUiState.isEdited && !messageUiState.isProcessing)
        }
    }

    override fun getLayout(): Int = R.layout.item_message_incoming

    override fun initializeViewBinding(view: View) = ItemMessageIncomingBinding.bind(view)
}