package com.linc.inphoto.ui.imageeditor.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemCropRatioBinding
import com.linc.inphoto.ui.imageeditor.RatioUiState
import com.linc.inphoto.utils.extensions.view.select
import com.xwray.groupie.viewbinding.BindableItem

class CropRatioItem(
    private val ratioUiState: RatioUiState
) : BindableItem<ItemCropRatioBinding>() {
    override fun bind(viewBinding: ItemCropRatioBinding, position: Int) {
        viewBinding.aspectRatioPreview.apply {
            setAspectRatio(ratioUiState.width, ratioUiState.height)
            select(ratioUiState.selected)
            setOnClickListener {
                ratioUiState.onClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_crop_ratio

    override fun initializeViewBinding(view: View) = ItemCropRatioBinding.bind(view)
}