package com.linc.inphoto.ui.gallery.item

import android.graphics.Color
import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemGalleryImageBinding
import com.linc.inphoto.ui.gallery.model.ImageUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class GalleryImageItem(
    private val imageUiState: ImageUiState
) : BindableItem<ItemGalleryImageBinding>() {

    override fun bind(viewBinding: ItemGalleryImageBinding, position: Int) {
        with(viewBinding) {
            galleryImageView.loadImage(
                image = imageUiState.uri,
                placeholderTint = Color.BLACK,
                errorTint = Color.BLACK
            )
            root.setOnClickListener {
                imageUiState.onClick()
            }
        }

    }

    override fun getLayout() = R.layout.item_gallery_image

    override fun initializeViewBinding(view: View) = ItemGalleryImageBinding.bind(view)
}