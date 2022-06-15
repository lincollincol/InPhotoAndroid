package com.linc.inphoto.ui.home.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemHomePostBinding
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.autoAnimateTargets
import com.linc.inphoto.utils.extensions.pattern.DATE_PATTERN_DMY_DOT
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

class HomePostItem(
    private val homePostUiState: HomePostUiState
) : BindableItem<ItemHomePostBinding>(homePostUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemHomePostBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.apply {
                loadImage(homePostUiState.userAvatarUrl)
                setOnThrottledClickListener { homePostUiState.onProfile() }
            }
            postImageView.apply {
                loadImage(homePostUiState.contentUrl)
                setOnThrottledClickListener { homePostUiState.onImage() }
                setOnDoubleClickListener(
                    onSingleClick = { homePostUiState.onImage() },
                    onDoubleClick = {
                        autoAnimateTargets(root, likeAnimationView)
                        likeImageView.select()
                        likeAnimationView.playOneTime { homePostUiState.onDoubleTap() }
                    }
                )
            }
            usernameTextView.apply {
                text = homePostUiState.username
                setOnThrottledClickListener { homePostUiState.onProfile() }
            }
            descriptionTextView.text = homePostUiState.description
            bookmarkImageView.apply {
                select(homePostUiState.isBookmarked)
                setOnClickListener {
                    toggleSelect()
                    homePostUiState.onBookmark()
                }
            }
            likeImageView.apply {
                select(homePostUiState.isLiked)
                setOnClickListener {
                    toggleSelect()
                    homePostUiState.onLike()
                }
            }
            commentImageView.setOnClickListener { homePostUiState.onComment() }
            moreImageView.setOnClickListener { homePostUiState.onMore() }
            dateTextView.text = DateFormatter.format(
                homePostUiState.createdTimestamp,
                DATE_PATTERN_DMY_DOT
            )
            tagsChipGroup.addChips(homePostUiState.tags, R.layout.item_tag_chip)
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemHomePostBinding>) {
        super.unbind(viewHolder)
        with(viewHolder.binding) {
            postImageView.clearImage()
            userAvatarImageView.clearImage()
        }
    }

    override fun getLayout(): Int = R.layout.item_home_post

    override fun initializeViewBinding(view: View) = ItemHomePostBinding.bind(view)
}