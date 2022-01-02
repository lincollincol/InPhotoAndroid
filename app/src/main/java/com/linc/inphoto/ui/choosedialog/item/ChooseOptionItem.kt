package com.linc.inphoto.ui.choosedialog.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemChooseOptionBinding
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.utils.extensions.enable
import com.xwray.groupie.viewbinding.BindableItem

class ChooseOptionItem(
    private val option: ChooseOptionModel
) : BindableItem<ItemChooseOptionBinding>() {
    override fun bind(viewBinding: ItemChooseOptionBinding, position: Int) {
        viewBinding.chooseOption.apply {
            setText(option.value)
            enable(option.enabled)
        }
    }

    override fun getLayout() = R.layout.item_choose_option

    override fun initializeViewBinding(view: View) = ItemChooseOptionBinding.bind(view)
}