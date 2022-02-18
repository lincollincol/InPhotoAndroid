package com.linc.inphoto.ui.gallery.item

import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemGalleryImageBinding
import com.linc.inphoto.ui.gallery.ImageUiState
import com.linc.inphoto.utils.extensions.view.IMAGE_BLUR_SMALL
import com.linc.inphoto.utils.extensions.view.THUMB_SMALL
import com.linc.inphoto.utils.extensions.view.loadUriImage
import com.xwray.groupie.viewbinding.BindableItem

class GalleryImageItem(
    private val imageUiState: ImageUiState
) : BindableItem<ItemGalleryImageBinding>() {

    override fun bind(viewBinding: ItemGalleryImageBinding, position: Int) {
        with(viewBinding) {
            galleryImageView.loadUriImage(
                uri = imageUiState.uri,
                diskCacheStrategy = DiskCacheStrategy.ALL
            )
            blurredImageView.loadUriImage(
                uri = imageUiState.uri,
                size = THUMB_SMALL,
                blurRadius = IMAGE_BLUR_SMALL,
                placeholder = 0,
                errorPlaceholder = 0,
                diskCacheStrategy = DiskCacheStrategy.ALL
            )
            root.setOnClickListener {
                imageUiState.onClick()
            }
        }

    }

    override fun getLayout() = R.layout.item_gallery_image

    override fun initializeViewBinding(view: View) = ItemGalleryImageBinding.bind(view)
}