package com.linc.inphoto.ui.gallery.item

import android.graphics.Color
import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemGalleryImageBinding
import com.linc.inphoto.ui.gallery.ImageUiState
import com.linc.inphoto.utils.extensions.view.IMAGE_BLUR_SMALL
import com.linc.inphoto.utils.extensions.view.THUMB_SMALL
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
            blurredImageView.loadImage(
                image = imageUiState.uri,
                size = THUMB_SMALL,
                blurRadius = IMAGE_BLUR_SMALL,
                errorPlaceholder = null
            )
            root.setOnClickListener {
                imageUiState.onClick()
            }
        }

    }

    override fun getLayout() = R.layout.item_gallery_image

    override fun initializeViewBinding(view: View) = ItemGalleryImageBinding.bind(view)
}