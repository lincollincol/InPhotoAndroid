package com.linc.inphoto.ui.profilefollowers.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemFollowerUserBinding
import com.linc.inphoto.ui.profilefollowers.model.FollowerUserUiState
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class FollowerUserItem(
    private val followerUserUiState: FollowerUserUiState
) : BindableItem<ItemFollowerUserBinding>(followerUserUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemFollowerUserBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(followerUserUiState.avatarUrl)
            nameTextView.text = followerUserUiState.username
            statusTextView.text = followerUserUiState.status
            root.setOnThrottledClickListener { followerUserUiState.onClick() }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemFollowerUserBinding>) {
        super.unbind(viewHolder)
        viewHolder.binding.avatarImageView.clearImage()
    }

    override fun getLayout(): Int = R.layout.item_follower_user

    override fun initializeViewBinding(view: View) = ItemFollowerUserBinding.bind(view)
}