package com.linc.inphoto.ui.home.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemNewStoryBinding
import com.linc.inphoto.ui.home.model.NewStoryUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class NewStoryItem(
    private val storyUiState: NewStoryUiState
) : BindableItem<ItemNewStoryBinding>(storyUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemNewStoryBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(storyUiState.userAvatarUrl, blurRadius = 20)
            root.setOnThrottledClickListener { storyUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_new_story

    override fun initializeViewBinding(view: View) = ItemNewStoryBinding.bind(view)
}