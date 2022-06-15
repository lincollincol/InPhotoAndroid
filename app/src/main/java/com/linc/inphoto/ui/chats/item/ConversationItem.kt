package com.linc.inphoto.ui.chats.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChatBinding
import com.linc.inphoto.ui.chats.model.ConversationUiState
import com.linc.inphoto.ui.chats.model.isEmptyConversation
import com.linc.inphoto.ui.chats.model.isLastMessageAttachmentsOnly
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.context
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import java.util.*

class ConversationItem(
    private val conversationUiState: ConversationUiState
) : BindableItem<ItemChatBinding>(conversationUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemChatBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.apply {
                loadImage(conversationUiState.avatarUrl)
                setOnThrottledClickListener { conversationUiState.onUserClick() }
            }
            nameTextView.text = conversationUiState.username
            lastMessageTextView.text = when {
                conversationUiState.isEmptyConversation -> context.getString(R.string.no_messages)
                conversationUiState.isLastMessageAttachmentsOnly -> context.getString(R.string.attachments)
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
                    conversationUiState.onMenuClick()
                    return@setOnLongClickListener false
                }
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemChatBinding>) {
        super.unbind(viewHolder)
        viewHolder.binding.avatarImageView.clearImage()
    }

    override fun getLayout(): Int = R.layout.item_chat

    override fun initializeViewBinding(view: View) = ItemChatBinding.bind(view)
}