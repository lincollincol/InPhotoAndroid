package com.linc.inphoto.ui.languagesettings.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemLaguageBinding
import com.linc.inphoto.ui.languagesettings.model.LanguageUiState
import com.linc.inphoto.utils.extensions.capitalized
import com.linc.inphoto.utils.extensions.flagEmoji
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.xwray.groupie.viewbinding.BindableItem

class LanguageItem(
    private val languageUiState: LanguageUiState
) : BindableItem<ItemLaguageBinding>(languageUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemLaguageBinding, position: Int) {
        with(viewBinding) {
            flagTextView.text = languageUiState.locale.flagEmoji
            languageTextView.text = languageUiState.locale.displayLanguage.capitalized()
            root.setOnThrottledClickListener {
                languageUiState.onClick()
            }
            currentImageView.show(languageUiState.isCurrentLocale)
        }
    }

    override fun getLayout(): Int = R.layout.item_laguage

    override fun initializeViewBinding(view: View) = ItemLaguageBinding.bind(view)

}