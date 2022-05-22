package com.linc.inphoto.ui.home.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemUserStoryBinding
import com.linc.inphoto.ui.home.model.UserStoryUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class UserStoryItem(
    private val storyUiState: UserStoryUiState
) : BindableItem<ItemUserStoryBinding>(storyUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemUserStoryBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(storyUiState.userAvatarUrl)
            nameTextView.text = storyUiState.username
        }
    }

    override fun getLayout(): Int = R.layout.item_user_story

    override fun initializeViewBinding(view: View) = ItemUserStoryBinding.bind(view)
}