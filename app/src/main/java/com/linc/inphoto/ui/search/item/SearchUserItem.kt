package com.linc.inphoto.ui.search.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemSearchUserBinding
import com.linc.inphoto.ui.search.model.SearchUserUiState
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class SearchUserItem(
    private val searchUserUiState: SearchUserUiState
) : BindableItem<ItemSearchUserBinding>(searchUserUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemSearchUserBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(searchUserUiState.avatarUrl)
            nameTextView.text = searchUserUiState.username
            statusTextView.apply {
                text = searchUserUiState.status
                show(searchUserUiState.status.isNotEmpty())
            }
            followButton.apply {
                select(searchUserUiState.isFollowing)
                setOnThrottledClickListener { searchUserUiState.onFollow() }
            }
            root.setOnThrottledClickListener { searchUserUiState.onClick() }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemSearchUserBinding>) {
        super.unbind(viewHolder)
        viewHolder.binding.avatarImageView.clearImage()
    }

    override fun getLayout(): Int = R.layout.item_search_user

    override fun initializeViewBinding(view: View) = ItemSearchUserBinding.bind(view)

}