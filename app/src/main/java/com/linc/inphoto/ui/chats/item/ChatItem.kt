package com.linc.inphoto.ui.chats.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChatBinding
import com.linc.inphoto.ui.chats.model.ChatEntityUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class ChatItem(
    private val chatEntityUiState: ChatEntityUiState
) : BindableItem<ItemChatBinding>(chatEntityUiState.username.hashCode().toLong()) {

    override fun bind(viewBinding: ItemChatBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(chatEntityUiState.avatarUrl)
            nameTextView.text = chatEntityUiState.username
            lastMessageTextView.text = chatEntityUiState.lastMessage
            lastMessageTimeTextView.text = DateFormatter.getRelativeTimeSpanString2(
                chatEntityUiState.lastMessageTimestamp,
                Locale.US
            )
            root.setOnThrottledClickListener { chatEntityUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_chat

    override fun initializeViewBinding(view: View) = ItemChatBinding.bind(view)
}