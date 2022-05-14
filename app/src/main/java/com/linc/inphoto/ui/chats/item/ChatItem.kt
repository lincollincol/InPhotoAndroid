package com.linc.inphoto.ui.chats.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChatBinding
import com.linc.inphoto.ui.chats.model.ChatEntityUiState
import com.linc.inphoto.ui.chats.model.isEmptyConversation
import com.linc.inphoto.ui.chats.model.isLastMessageAttachmentsOnly
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.getString
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class ChatItem(
    private val chatEntityUiState: ChatEntityUiState
) : BindableItem<ItemChatBinding>(chatEntityUiState.username.hashCode().toLong()) {

    override fun bind(viewBinding: ItemChatBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(chatEntityUiState.avatarUrl)
            nameTextView.text = chatEntityUiState.username
            lastMessageTextView.text = when {
                chatEntityUiState.isEmptyConversation -> getString(R.string.no_messages)
                chatEntityUiState.isLastMessageAttachmentsOnly -> getString(R.string.attachments)
                else -> chatEntityUiState.lastMessage
            }
            lastMessageTimeTextView.apply {
                text = DateFormatter.getRelativeTimeSpanString2(
                    chatEntityUiState.lastMessageTimestamp,
                    Locale.US
                )
                show(!chatEntityUiState.isEmptyConversation)
            }
            root.setOnThrottledClickListener { chatEntityUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_chat

    override fun initializeViewBinding(view: View) = ItemChatBinding.bind(view)
}