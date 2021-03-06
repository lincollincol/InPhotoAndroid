package com.linc.inphoto.ui.profile.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostPreviewBinding
import com.linc.inphoto.ui.profile.model.ProfilePostUiState
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class ProfilePostItem(
    private val profilePostUiState: ProfilePostUiState
) : BindableItem<ItemPostPreviewBinding>(profilePostUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemPostPreviewBinding, position: Int) {
        with(viewBinding) {
            postImageView.loadImage(profilePostUiState.post.contentUrl)
            root.setOnThrottledClickListener {
                profilePostUiState.onClick()
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemPostPreviewBinding>) {
        super.unbind(viewHolder)
        with(viewHolder.binding) {
            postImageView.clearImage()
        }
    }

    override fun getLayout() = R.layout.item_post_preview

    override fun initializeViewBinding(view: View) = ItemPostPreviewBinding.bind(view)
}