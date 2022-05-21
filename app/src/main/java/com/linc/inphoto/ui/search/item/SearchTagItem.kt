package com.linc.inphoto.ui.search.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemSearchTagBinding
import com.linc.inphoto.ui.search.model.SearchTagUiState
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class SearchTagItem(
    private val searchTagUiState: SearchTagUiState
) : BindableItem<ItemSearchTagBinding>(searchTagUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemSearchTagBinding, position: Int) {
        with(viewBinding) {
            nameTextView.text = searchTagUiState.tag
            root.setOnThrottledClickListener { searchTagUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_search_tag

    override fun initializeViewBinding(view: View) = ItemSearchTagBinding.bind(view)

}