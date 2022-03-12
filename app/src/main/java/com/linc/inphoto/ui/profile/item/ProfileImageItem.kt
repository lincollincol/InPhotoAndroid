package com.linc.inphoto.ui.profile.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemProfileImageBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProfileImageItem(
) : BindableItem<ItemProfileImageBinding>() {

    override fun bind(viewBinding: ItemProfileImageBinding, position: Int) {
    }

    override fun getLayout() = R.layout.item_profile_image

    override fun initializeViewBinding(view: View) = ItemProfileImageBinding.bind(view)
}