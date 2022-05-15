package com.linc.inphoto.ui.chats.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChatsTitleBinding
import com.xwray.groupie.viewbinding.BindableItem

class ChatsTitleItem(
    private val title: String
) : BindableItem<ItemChatsTitleBinding>(title.hashCode().toLong()) {
    override fun bind(viewBinding: ItemChatsTitleBinding, position: Int) {
        viewBinding.titleTextView.text = title
    }

    override fun getLayout(): Int = R.layout.item_chats_title

    override fun initializeViewBinding(view: View) = ItemChatsTitleBinding.bind(view)
}