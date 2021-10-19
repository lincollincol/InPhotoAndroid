package com.linc.inphoto.ui.profile.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemProfilePhotoBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProfilePhotoItem(
) : BindableItem<ItemProfilePhotoBinding>() {

    override fun bind(viewBinding: ItemProfilePhotoBinding, position: Int) {
    }

    override fun getLayout() = R.layout.item_profile_photo

    override fun initializeViewBinding(view: View) = ItemProfilePhotoBinding.bind(view)
}