package com.linc.inphoto.ui.postsoverview.item

import android.util.Size
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostOverviewBinding
import com.linc.inphoto.ui.postsoverview.PostUiState
import com.linc.inphoto.utils.extensions.compactNumber
import com.linc.inphoto.utils.extensions.view.addChip
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.select
import com.xwray.groupie.viewbinding.BindableItem

class PostOverviewItem(
    private val postUiState: PostUiState
) : BindableItem<ItemPostOverviewBinding>(postUiState.postId.hashCode().toLong()) {

    override fun bind(viewBinding: ItemPostOverviewBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.loadImage(
                postUiState.userAvatarUrl,
                size = Size(256, 256),
                diskCacheStrategy = DiskCacheStrategy.ALL
            )
            postImageView.loadImage(
                postUiState.contentUrl,
                size = Size(512, 512),
                diskCacheStrategy = DiskCacheStrategy.ALL
            )
            usernameTextView.text = postUiState.username
            descriptionTextView.text = postUiState.description
            bookmarkImageView.apply {
                setOnClickListener { postUiState.onBookmark() }
            }
            likeImageTextView.apply {
                setText(postUiState.likesCount.compactNumber())
                select(postUiState.isLiked)
                setOnIconClickListener { postUiState.onLike() }
            }
            commentImageView.apply {
                setText(postUiState.commentsCount.compactNumber())
                setOnIconClickListener { postUiState.onComment() }
            }
            postUiState.tags.forEach { tag ->
                tagsChipGroup.addChip(tag, R.layout.item_tag_chip)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_post_overview

    override fun initializeViewBinding(view: View) = ItemPostOverviewBinding.bind(view)
}