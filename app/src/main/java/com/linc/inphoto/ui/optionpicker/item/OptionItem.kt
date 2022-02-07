package com.linc.inphoto.ui.optionpicker.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemOptionBinding
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.utils.extensions.enable
import com.linc.inphoto.utils.extensions.show
import com.xwray.groupie.viewbinding.BindableItem

class OptionItem(
    private val option: OptionModel
) : BindableItem<ItemOptionBinding>() {
    override fun bind(viewBinding: ItemOptionBinding, position: Int) {
        with(viewBinding) {
            optionTextView.apply {
                setText(option.value)
                enable(option.enabled)
            }
            optionIcon.apply {
                if (option.icon != null) {
                    show()
                    setImageResource(option.icon!!)
                }
            }
        }

    }

    override fun getLayout() = R.layout.item_option

    override fun initializeViewBinding(view: View) = ItemOptionBinding.bind(view)
}