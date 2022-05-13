package com.linc.inphoto.ui.mediareview.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemMediaReviewBinding
import com.linc.inphoto.ui.mediareview.model.MediaFileUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class MediaReviewItem(
    private val mediaUiState: MediaFileUiState
) : BindableItem<ItemMediaReviewBinding>() {
    override fun bind(viewBinding: ItemMediaReviewBinding, position: Int) {
        viewBinding.fileImageView.loadImage(mediaUiState.uri)
    }

    override fun getLayout(): Int = R.layout.item_media_review

    override fun initializeViewBinding(view: View) = ItemMediaReviewBinding.bind(view)
}