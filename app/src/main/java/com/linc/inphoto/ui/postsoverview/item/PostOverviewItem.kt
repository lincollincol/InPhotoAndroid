package com.linc.inphoto.ui.postsoverview.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostOverviewBinding
import com.linc.inphoto.ui.postsoverview.PostUiState
import com.linc.inphoto.utils.extensions.compactNumber
import com.linc.inphoto.utils.extensions.view.addChip
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class PostOverviewItem(
    private val postUiState: PostUiState
) : BindableItem<ItemPostOverviewBinding>() {

    override fun bind(viewBinding: ItemPostOverviewBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.loadImage(postUiState.post.userAvatarUrl)
            postImageView.loadImage(postUiState.post.contentUrl)
            usernameTextView.text = postUiState.post.username
            descriptionTextView.text = postUiState.post.description
            bookmarkImageView.apply {
                setOnClickListener { postUiState.onBookmark() }
            }
            likeImageTextView.apply {
                setText(postUiState.post.likesCount.compactNumber())
                setOnIconClickListener { postUiState.onLike() }
            }
            commentImageView.apply {
                setText(postUiState.post.commentsCount.compactNumber())
                setOnIconClickListener { postUiState.onComment() }
            }
            postUiState.post.tags.forEach { tag ->
                tagsChipGroup.addChip(tag, R.layout.item_tag_chip)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_post_overview

    override fun initializeViewBinding(view: View) = ItemPostOverviewBinding.bind(view)
}