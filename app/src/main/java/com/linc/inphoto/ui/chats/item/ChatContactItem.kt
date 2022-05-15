package com.linc.inphoto.ui.chats.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChatContactBinding
import com.linc.inphoto.ui.chats.model.ChatContactUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class ChatContactItem(
    private val chatContactUiState: ChatContactUiState
) : BindableItem<ItemChatContactBinding>(chatContactUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemChatContactBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.apply {
                loadImage(chatContactUiState.avatarUrl)
                setOnThrottledClickListener { chatContactUiState.onUserClick() }
            }
            nameTextView.text = chatContactUiState.username
            root.setOnThrottledClickListener { chatContactUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_chat_contact

    override fun initializeViewBinding(view: View) = ItemChatContactBinding.bind(view)
}