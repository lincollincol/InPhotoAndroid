package com.linc.inphoto.ui.home.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemSingleStoryBinding
import com.linc.inphoto.ui.home.model.SingleStoryUiState
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class SingleStoryItem(
    private val storyUiState: SingleStoryUiState
) : BindableItem<ItemSingleStoryBinding>(storyUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemSingleStoryBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(storyUiState.userAvatarUrl)
            nameTextView.text = storyUiState.username
            root.setOnThrottledClickListener { storyUiState.onClick() }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemSingleStoryBinding>) {
        super.unbind(viewHolder)
        viewHolder.binding.avatarImageView.clearImage()
    }

    override fun getLayout(): Int = R.layout.item_single_story

    override fun initializeViewBinding(view: View) = ItemSingleStoryBinding.bind(view)
}