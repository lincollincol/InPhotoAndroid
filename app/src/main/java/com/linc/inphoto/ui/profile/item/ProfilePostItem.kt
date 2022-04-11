package com.linc.inphoto.ui.profile.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemProfilePostBinding
import com.linc.inphoto.ui.profile.ProfilePostUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class ProfilePostItem(
    private val profilePostUiState: ProfilePostUiState
) : BindableItem<ItemProfilePostBinding>(profilePostUiState.post.id.hashCode().toLong()) {

    override fun bind(viewBinding: ItemProfilePostBinding, position: Int) {
        with(viewBinding) {
            postImageView.loadImage(
                image = profilePostUiState.post.contentUrl/*,
                size = Size(256, 256)*/
            )
            root.setOnClickListener {
                profilePostUiState.onClick()
            }
        }
    }

    override fun getLayout() = R.layout.item_profile_post

    override fun initializeViewBinding(view: View) = ItemProfilePostBinding.bind(view)
}