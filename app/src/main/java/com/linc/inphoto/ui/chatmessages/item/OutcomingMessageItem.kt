package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMessageOutcomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.ui.chatmessages.model.hasMultipleAttachments
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.bindWidthTo
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class OutcomingMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemMessageOutcomingBinding>(messageUiState.id.hashCode().toLong()) {
    override fun bind(viewBinding: ItemMessageOutcomingBinding, position: Int) {
        with(viewBinding) {
            messageTextView.apply {
                text = messageUiState.text
                show(messageUiState.text.isNotEmpty())
            }
            timeTextView.text = DateFormatter.format(
                messageUiState.createdTimestamp,
                TIME_PATTERN_SEMICOLON,
                Locale.US
            )
            fileImageView.apply {
                bindWidthTo(imageWidthView)
                loadImage(
                    messageUiState.files.firstOrNull(),
                    diskCacheStrategy = DiskCacheStrategy.ALL
                )
                show(messageUiState.files.isNotEmpty())
                fileImageView.setOnThrottledClickListener { messageUiState.onImageClick() }
                setOnLongClickListener {
                    messageUiState.onClick()
                    return@setOnLongClickListener false
                }
            }
            showAllButton.apply {
                show(messageUiState.hasMultipleAttachments)
                setOnThrottledClickListener { messageUiState.onImageClick() }
            }
            root.setOnThrottledClickListener { messageUiState.onClick() }
            pendingProgressBar.show(messageUiState.isProcessing)
            editedTextView.show(messageUiState.isEdited && !messageUiState.isProcessing)
        }
    }

    override fun getLayout(): Int = R.layout.item_message_outcoming

    override fun initializeViewBinding(view: View) = ItemMessageOutcomingBinding.bind(view)
}