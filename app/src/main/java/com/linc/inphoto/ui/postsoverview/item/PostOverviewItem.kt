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
) : BindableItem<ItemPostOverviewBinding>(postUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemPostOverviewBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.loadImage(postUiState.userAvatarUrl)
            postImageView.apply {
                loadImage(postUiState.contentUrl)
                setOnDoubleClickListener {
                    autoAnimateTargets(root, likeAnimationView)
                    likeImageTextView.select()
                    likeAnimationView.playOneTime { postUiState.onDoubleTap() }
                }
            }
            usernameTextView.text = postUiState.username
            descriptionTextView.text = postUiState.description
            bookmarkImageView.apply {
                select(postUiState.isBookmarked)
                setOnClickListener {
                    toggleSelect()
                    postUiState.onBookmark()
                }
            }
            likeImageTextView.apply {
                select(postUiState.isLiked)
                setOnClickListener {
                    toggleSelect()
                    postUiState.onLike()
                }
            }
            commentImageView.apply {
                setOnClickListener { postUiState.onComment() }
            }
            tagsChipGroup.addChips(postUiState.tags, R.layout.item_tag_chip)
            moreImageView.setOnClickListener { postUiState.onMore() }
        }
    }

    override fun getLayout(): Int = R.layout.item_post_overview

    override fun initializeViewBinding(view: View) = ItemPostOverviewBinding.bind(view)
}