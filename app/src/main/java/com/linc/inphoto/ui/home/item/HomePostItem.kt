package com.linc.inphoto.ui.home.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemHomePostBinding
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.utils.extensions.autoAnimateTargets
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem

class HomePostItem(
    private val homePostUiState: HomePostUiState
) : BindableItem<ItemHomePostBinding>(homePostUiState.getStateItemId()) {

    override fun bind(viewBinding: ItemHomePostBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.loadImage(homePostUiState.userAvatarUrl)
            postImageView.apply {
                loadImage(homePostUiState.contentUrl)
                setOnDoubleClickListener {
                    autoAnimateTargets(root, likeAnimationView)
                    likeImageTextView.select()
                    likeAnimationView.playOneTime { homePostUiState.onDoubleTap() }
                }
            }
            usernameTextView.text = homePostUiState.username
            descriptionTextView.text = homePostUiState.description
            bookmarkImageView.apply {
                select(homePostUiState.isBookmarked)
                setOnClickListener {
                    toggleSelect()
                    homePostUiState.onBookmark()
                }
            }
            likeImageTextView.apply {
                select(homePostUiState.isLiked)
                setOnClickListener {
                    toggleSelect()
                    homePostUiState.onLike()
                }
            }
            commentImageView.apply {
                setOnClickListener { homePostUiState.onComment() }
            }
            tagsChipGroup.addChips(homePostUiState.tags, R.layout.item_tag_chip)
            moreImageView.setOnClickListener { homePostUiState.onMore() }
        }
    }

    override fun getLayout(): Int = R.layout.item_home_post

    override fun initializeViewBinding(view: View) = ItemHomePostBinding.bind(view)
}