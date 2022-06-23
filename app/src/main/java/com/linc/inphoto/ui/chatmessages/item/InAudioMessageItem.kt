package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemAudioMessageIncomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem

class InAudioMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemAudioMessageIncomingBinding>(messageUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemAudioMessageIncomingBinding, position: Int) {
        with(viewBinding) {
            messageTextView.apply {
                text = messageUiState.text
                show(messageUiState.text.isNotEmpty())
            }
            timeTextView.text = DateFormatter.format(
                messageUiState.createdTimestamp,
                TIME_PATTERN_SEMICOLON
            )
            playButton.apply {
                setImageResource(
                    when {
                        messageUiState.isAudioPlaying -> R.drawable.ic_pause
                        else -> R.drawable.ic_play_arrow
                    }
                )
                setOnThrottledClickListener {
                    messageUiState.onAudioClick()
                }
            }
            titleTextView.text = messageUiState.attachment?.name
            root.setOnThrottledClickListener { messageUiState.onClick() }
            processingProgressBar.show(messageUiState.isProcessing)
            editedTextView.show(messageUiState.isEdited && !messageUiState.isProcessing)
        }
    }

    override fun getLayout(): Int = R.layout.item_audio_message_incoming

    override fun initializeViewBinding(view: View) = ItemAudioMessageIncomingBinding.bind(view)
}