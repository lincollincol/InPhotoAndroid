package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemImageMessageOutcomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.ui.chatmessages.model.hasAttachments
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem

class OutImageMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemImageMessageOutcomingBinding>(messageUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemImageMessageOutcomingBinding, position: Int) {
        with(viewBinding) {
            fileImageView.apply {
                loadImage(
                    messageUiState.attachment?.url,
                    overrideOriginalSize = true,
                    diskCacheStrategy = DiskCacheStrategy.ALL
                )
                show(messageUiState.hasAttachments)
                fileImageView.setOnThrottledClickListener { messageUiState.onImageClick() }
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

    override fun getLayout(): Int = R.layout.item_image_message_outcoming

    override fun initializeViewBinding(view: View) = ItemImageMessageOutcomingBinding.bind(view)
}