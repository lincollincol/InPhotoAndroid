package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemVideoMessageOutcomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.ui.chatmessages.model.hasAttachments
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem

class OutVideoMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemVideoMessageOutcomingBinding>(messageUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemVideoMessageOutcomingBinding, position: Int) {
        with(viewBinding) {
            fileImageView.apply {
                loadImage(
                    messageUiState.attachment?.uri,
                    size = THUMB_SMALL,
                    blurRadius = IMAGE_BLUR_LARGE,
                    overrideOriginalSize = true,
                    diskCacheStrategy = DiskCacheStrategy.ALL
                )
                show(messageUiState.hasAttachments)
                playButton.setOnThrottledClickListener { messageUiState.onVideoClick() }
                fileImageView.setOnThrottledClickListener { messageUiState.onVideoClick() }
                setOnLongClickListener {
                    messageUiState.onClick()
                    return@setOnLongClickListener false
                }
            }
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

    override fun getLayout(): Int = R.layout.item_video_message_outcoming

    override fun initializeViewBinding(view: View) = ItemVideoMessageOutcomingBinding.bind(view)
}