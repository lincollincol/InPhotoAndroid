package com.linc.inphoto.ui.postsoverview.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostOverviewBinding
import com.linc.inphoto.ui.postsoverview.PostUiState
import com.linc.inphoto.utils.extensions.autoAnimateTargets
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem

class PostOverviewItem(
    private val postUiState: PostUiState
) : BindableItem<ItemPostOverviewBinding>(postUiState.postId.hashCode().toLong()) {

    override fun bind(viewBinding: ItemPostOverviewBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.loadImage(postUiState.userAvatarUrl)
            postImageView.apply {
                loadImage(postUiState.contentUrl)
                setOnDoubleClickListener {
                    autoAnimateTargets(root, likeAnimationView)
                    likeAnimationView.playOneTime { postUiState.onLike() }
                }
            }
            usernameTextView.text = postUiState.username
            descriptionTextView.text = postUiState.description
            bookmarkImageView.apply {
                select(postUiState.isBookmarked)
                setOnClickListener { postUiState.onBookmark() }
            }
            likeImageTextView.apply {
                select(postUiState.isLiked)
                setOnClickListener { postUiState.onLike() }
            }
            commentImageView.apply {
                setOnClickListener { postUiState.onComment() }
            }
            tagsChipGroup.addChips(postUiState.tags, R.layout.item_tag_chip)
        }
    }

    override fun getLayout(): Int = R.layout.item_post_overview

    override fun initializeViewBinding(view: View) = ItemPostOverviewBinding.bind(view)
}