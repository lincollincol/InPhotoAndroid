package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMessageIncomingBinding
import com.linc.inphoto.ui.chatmessages.model.MessageUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.view.bindWidthTo
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class IncomingMessageItem(
    private val messageUiState: MessageUiState
) : BindableItem<ItemMessageIncomingBinding>(messageUiState.id.hashCode().toLong()) {
    override fun bind(viewBinding: ItemMessageIncomingBinding, position: Int) {
        with(viewBinding) {
            messageTextView.text = messageUiState.text
            timeTextView.text = DateFormatter.format(
                messageUiState.createdTimestamp,
                TIME_PATTERN_SEMICOLON,
                Locale.US
            )
            fileImageView.apply {
                bindWidthTo(imageWidthView)
                loadImage(messageUiState.files.firstOrNull())
                show(messageUiState.files.isNotEmpty())
            }
            showAllButton.show(messageUiState.files.count() > 1)
            fileImageView.setOnThrottledClickListener {
                messageUiState.onImageClick()
            }
            showAllButton.setOnThrottledClickListener {
                messageUiState.onImageClick()
            }
            root.setOnThrottledClickListener {
                messageUiState.onClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_message_incoming

    override fun initializeViewBinding(view: View) = ItemMessageIncomingBinding.bind(view)
}