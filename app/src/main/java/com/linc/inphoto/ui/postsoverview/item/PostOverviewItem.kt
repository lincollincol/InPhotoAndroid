package com.linc.inphoto.ui.postsoverview.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostOverviewBinding
import com.linc.inphoto.ui.postsoverview.PostUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.autoAnimateTargets
import com.linc.inphoto.utils.extensions.pattern.DATE_PATTERN_DMY_DOT
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class PostOverviewItem(
    private val postUiState: PostUiState
) : BindableItem<ItemPostOverviewBinding>(postUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemPostOverviewBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.apply {
                loadImage(postUiState.userAvatarUrl)
                setOnThrottledClickListener { postUiState.onProfile() }
            }
            postImageView.apply {
                loadImage(postUiState.contentUrl)
                setOnDoubleClickListener(
                    onSingleClick = { postUiState.onImage() },
                    onDoubleClick = {
                        autoAnimateTargets(root, likeAnimationView)
                        likeImageView.select()
                        likeAnimationView.playOneTime { postUiState.onDoubleTap() }
                    }
                )
            }
            usernameTextView.apply {
                text = postUiState.username
                setOnThrottledClickListener { postUiState.onProfile() }
            }
            descriptionTextView.text = postUiState.description
            bookmarkImageView.apply {
                select(postUiState.isBookmarked)
                setOnClickListener {
                    toggleSelect()
                    postUiState.onBookmark()
                }
            }
            likeImageView.apply {
                select(postUiState.isLiked)
                setOnClickListener {
                    toggleSelect()
                    postUiState.onLike()
                }
            }
            commentImageView.setOnClickListener { postUiState.onComment() }
            moreImageView.setOnClickListener { postUiState.onMore() }
            dateTextView.text = DateFormatter.format(
                postUiState.createdTimestamp,
                DATE_PATTERN_DMY_DOT
            )
            tagsChipGroup.addChips(postUiState.tags, R.layout.item_tag_chip)
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemPostOverviewBinding>) {
        super.unbind(viewHolder)
        with(viewHolder.binding) {
            postImageView.clearImage()
            userAvatarImageView.clearImage()
        }
    }

    override fun getLayout(): Int = R.layout.item_post_overview

    override fun initializeViewBinding(view: View) = ItemPostOverviewBinding.bind(view)
}