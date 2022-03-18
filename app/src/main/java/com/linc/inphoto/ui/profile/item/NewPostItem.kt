package com.linc.inphoto.ui.profile.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemNewUserPostBinding
import com.xwray.groupie.viewbinding.BindableItem

class NewPostItem(
    private val newPostUiState: NewPostUiState
) : BindableItem<ItemNewUserPostBinding>(NewPostItem::class.simpleName.hashCode().toLong()) {
    override fun bind(viewBinding: ItemNewUserPostBinding, position: Int) {
        viewBinding.newPostButton.setOnClickListener {
            newPostUiState.onClick()
        }
    }

    override fun getLayout(): Int = R.layout.item_new_user_post

    override fun initializeViewBinding(view: View) = ItemNewUserPostBinding.bind(view)
}