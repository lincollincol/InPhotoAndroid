package com.linc.inphoto.ui.settings.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemSettingsOptionBinding
import com.linc.inphoto.ui.settings.model.SettingsOptionUiState
import com.xwray.groupie.viewbinding.BindableItem

class SettingsOptionItem(
    private val state: SettingsOptionUiState
) : BindableItem<ItemSettingsOptionBinding>(state.entry.hashCode().toLong()) {
    override fun bind(viewBinding: ItemSettingsOptionBinding, position: Int) {
        with(viewBinding) {
            optionImageView.setImageResource(state.entry.icon)
            optionTextView.setText(state.entry.title)
            root.setOnClickListener {
                state.onClick()
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_settings_option

    override fun initializeViewBinding(view: View) = ItemSettingsOptionBinding.bind(view)
}