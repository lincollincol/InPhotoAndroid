package com.linc.inphoto.ui.postcomments.item

import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostCommentBinding
import com.linc.inphoto.ui.postcomments.model.CommentUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.addBoldSpan
import com.linc.inphoto.utils.extensions.pattern.FORMAT_USER_COMMENT
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import java.util.*

class PostCommentItem(
    private val commentUiState: CommentUiState
) : BindableItem<ItemPostCommentBinding>(commentUiState.commentId.hashCode().toLong()) {

    override fun bind(viewBinding: ItemPostCommentBinding, position: Int) {
        with(viewBinding) {
            userAvatarImageView.apply {
                loadImage(commentUiState.userAvatarUrl)
                setOnThrottledClickListener {
                    commentUiState.onUserClicked()
                }
            }
            contentTextView.apply {
                text = String.format(
                    Locale.getDefault(),
                    FORMAT_USER_COMMENT,
                    commentUiState.username,
                    commentUiState.comment
                ).addBoldSpan(commentUiState.username)
                setWordsClickListener(
                    words = listOf(commentUiState.username),
                    onTargetClicked = {
                        commentUiState.onUserClicked()
                    },
                    onTextClicked = {
                        root.performPressed()
                        root.performClick()
                    }
                )
            }
            root.setOnThrottledClickListener { commentUiState.onCommentClicked() }
            dateTextView.text = DateFormatter.getRelativeTimeSpanString2(
                commentUiState.createdTimestamp
            )
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemPostCommentBinding>) {
        super.unbind(viewHolder)
        viewHolder.binding.userAvatarImageView.clearImage()
    }

    override fun getLayout(): Int = R.layout.item_post_comment

    override fun initializeViewBinding(view: View) = ItemPostCommentBinding.bind(view)

}