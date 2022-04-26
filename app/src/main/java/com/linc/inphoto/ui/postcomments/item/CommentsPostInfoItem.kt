package com.linc.inphoto.ui.postcomments.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemCommentsPostInfoBinding
import com.linc.inphoto.ui.postcomments.model.PostInfoUiState
import com.linc.inphoto.utils.extensions.view.addChips
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class CommentsPostInfoItem(
    private val postInfoUiState: PostInfoUiState
) : BindableItem<ItemCommentsPostInfoBinding>(postInfoUiState.username.hashCode().toLong()) {

    override fun bind(viewBinding: ItemCommentsPostInfoBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.loadImage(postInfoUiState.userAvatarUrl)
            usernameTextView.text = postInfoUiState.username
            descriptionTextView.text = postInfoUiState.description
            tagsChipGroup.addChips(postInfoUiState.tags, R.layout.item_tag_chip)
        }
    }

    override fun getLayout(): Int = R.layout.item_comments_post_info

    override fun initializeViewBinding(view: View) = ItemCommentsPostInfoBinding.bind(view)

}