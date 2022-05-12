package com.linc.inphoto.ui.chatmessages.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMessageAttachmentBinding
import com.linc.inphoto.ui.chatmessages.model.MessageAttachmentUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class MessageAttachmentItem(
    private val attachmentUiState: MessageAttachmentUiState
) : BindableItem<ItemMessageAttachmentBinding>(attachmentUiState.uri.hashCode().toLong()) {

    override fun bind(viewBinding: ItemMessageAttachmentBinding, position: Int) {
        with(viewBinding) {
            attachmentImageView.loadImage(attachmentUiState.uri)
            removeButton.setOnThrottledClickListener {
                attachmentUiState.onRemoveClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_message_attachment

    override fun initializeViewBinding(view: View) = ItemMessageAttachmentBinding.bind(view)

}