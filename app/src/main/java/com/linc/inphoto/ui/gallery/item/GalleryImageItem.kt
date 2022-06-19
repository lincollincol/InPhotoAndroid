package com.linc.inphoto.ui.gallery.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemGalleryImageBinding
import com.linc.inphoto.ui.gallery.model.ImageUiState
import com.linc.inphoto.utils.extensions.view.clearImage
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class GalleryImageItem(
    private val imageUiState: ImageUiState
) : BindableItem<ItemGalleryImageBinding>() {

    override fun bind(viewBinding: ItemGalleryImageBinding, position: Int) {
        with(viewBinding) {
            galleryImageView.loadImage(imageUiState.uri)
            root.setOnClickListener {
                imageUiState.onClick()
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemGalleryImageBinding>) {
        super.unbind(viewHolder)
        with(viewHolder.binding) {
            galleryImageView.clearImage()
        }
    }

    override fun getLayout() = R.layout.item_gallery_image

    override fun initializeViewBinding(view: View) = ItemGalleryImageBinding.bind(view)
}