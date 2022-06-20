package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemAudioMessageOutcomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.ui.chatmessages.model.hasMultipleAttachments
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem

class OutcomingAudioMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemAudioMessageOutcomingBinding>(messageUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemAudioMessageOutcomingBinding, position: Int) {
        with(viewBinding) {
            messageTextView.apply {
                text = messageUiState.text
                show(messageUiState.text.isNotEmpty())
            }
            timeTextView.text = DateFormatter.format(
                messageUiState.createdTimestamp,
                TIME_PATTERN_SEMICOLON
            )
            playButton.setOnThrottledClickListener {
                messageUiState.onAudioClick()
            }
            waveformView.setSampleFrom(intArrayOf(1, 1, 1, 0, 0, 0, 1, 1, 1))
            showAllButton.apply {
                show(messageUiState.hasMultipleAttachments)
                setOnThrottledClickListener { messageUiState.onImageClick() }
            }
            root.setOnThrottledClickListener { messageUiState.onClick() }
            processingProgressBar.show(messageUiState.isProcessing)
            editedTextView.show(messageUiState.isEdited && !messageUiState.isProcessing)
        }
    }

    override fun getLayout(): Int = R.layout.item_audio_message_outcoming

    override fun initializeViewBinding(view: View) = ItemAudioMessageOutcomingBinding.bind(view)
}