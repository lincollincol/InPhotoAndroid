package com.linc.inphoto.ui.cropimage.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemCropRatioBinding
import com.linc.inphoto.ui.cropimage.RatioUiState
import com.linc.inphoto.utils.extensions.pattern.FORMAT_ASPECT_RATIO_F_0_DGT
import com.linc.inphoto.utils.extensions.view.select
import com.xwray.groupie.viewbinding.BindableItem

class CropRatioItem(
    private val ratioUiState: RatioUiState
) : BindableItem<ItemCropRatioBinding>(ratioUiState.aspectRatio.hashCode().toLong()) {
    override fun bind(viewBinding: ItemCropRatioBinding, position: Int) {
        val aspectRatio = ratioUiState.aspectRatio
        viewBinding.aspectRatioPreview.apply {
            setAspectRatio(aspectRatio.width, aspectRatio.height)
            select(ratioUiState.selected)
            setOnClickListener {
                ratioUiState.onClick()
            }
        }
        viewBinding.aspectRatioTextView.text =
            String.format(FORMAT_ASPECT_RATIO_F_0_DGT, aspectRatio.width, aspectRatio.height)
    }

    override fun getLayout(): Int = R.layout.item_crop_ratio

    override fun initializeViewBinding(view: View) = ItemCropRatioBinding.bind(view)
}