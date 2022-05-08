package com.linc.inphoto.ui.search.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemSearchUserBinding
import com.linc.inphoto.ui.search.model.SearchUserUiState
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem

class SearchUserItem(
    private val searchUserUiState: SearchUserUiState
) : BindableItem<ItemSearchUserBinding>() {

    override fun bind(viewBinding: ItemSearchUserBinding, position: Int) {
        with(viewBinding) {
            avatarImageView.loadImage(searchUserUiState.avatarUrl)
            nameTextView.text = searchUserUiState.username
            statusTextView.text = searchUserUiState.status
            root.setOnThrottledClickListener { searchUserUiState.onClick() }
        }
    }

    override fun getLayout(): Int = R.layout.item_search_user

    override fun initializeViewBinding(view: View) = ItemSearchUserBinding.bind(view)

}