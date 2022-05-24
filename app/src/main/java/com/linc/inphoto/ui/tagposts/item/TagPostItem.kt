package com.linc.inphoto.ui.tagposts.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostPreviewBinding
import com.linc.inphoto.ui.tagposts.model.TagPostUiState
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class TagPostItem(
    private val tagPostUiState: TagPostUiState
) : BindableItem<ItemPostPreviewBinding>(tagPostUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemPostPreviewBinding, position: Int) {
        with(viewBinding) {
            postImageView.loadImage(tagPostUiState.postContentUrl)
            root.setOnThrottledClickListener {
                tagPostUiState.onClick()
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemPostPreviewBinding>) {
        super.unbind(viewHolder)
        viewHolder.binding.postImageView.clearImage()
    }

    override fun getLayout() = R.layout.item_post_preview

    override fun initializeViewBinding(view: View) = ItemPostPreviewBinding.bind(view)
}