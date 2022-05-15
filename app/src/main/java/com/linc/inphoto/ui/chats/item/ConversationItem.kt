package com.linc.inphoto.ui.chats.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChatBinding
import com.linc.inphoto.ui.chats.model.ConversationUiState
import com.linc.inphoto.ui.chats.model.isEmptyConversation
import com.linc.inphoto.ui.chats.model.isLastMessageAttachmentsOnly
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.getString
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

class ConversationItem(
    private val conversationUiState: ConversationUiState
) : BindableItem<ItemChatBinding>(conversationUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemChatBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(conversationUiState.avatarUrl)
            nameTextView.text = conversationUiState.username
            lastMessageTextView.text = when {
                conversationUiState.isEmptyConversation -> getString(R.string.no_messages)
                conversationUiState.isLastMessageAttachmentsOnly -> getString(R.string.attachments)
                else -> conversationUiState.lastMessage
            }
            lastMessageTimeTextView.apply {
                text = DateFormatter.getRelativeTimeSpanString2(
                    conversationUiState.lastMessageTimestamp,
                    Locale.US
                )
                show(!conversationUiState.isEmptyConversation)
            }
            root.apply {
                setOnThrottledClickListener { conversationUiState.onClick() }
                setOnLongClickListener {
                    conversationUiState.onLongClick()
                    return@setOnLongClickListener false
                }
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_chat

    override fun initializeViewBinding(view: View) = ItemChatBinding.bind(view)
}