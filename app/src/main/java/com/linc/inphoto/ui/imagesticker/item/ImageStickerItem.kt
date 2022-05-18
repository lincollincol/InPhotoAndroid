package com.linc.inphoto.ui.imagesticker.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemImageStickerBinding
import com.linc.inphoto.ui.imagesticker.model.StickerUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class ImageStickerItem(
    private val stickerUiState: StickerUiState
) : BindableItem<ItemImageStickerBinding>(stickerUiState.getStateItemId()) {
    override fun bind(viewBinding: ItemImageStickerBinding, position: Int) {
        with(viewBinding) {
            stickerImageView.loadImage(stickerUiState.uri)
            root.setOnThrottledClickListener { stickerUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_image_sticker

    override fun initializeViewBinding(view: View) = ItemImageStickerBinding.bind(view)
}