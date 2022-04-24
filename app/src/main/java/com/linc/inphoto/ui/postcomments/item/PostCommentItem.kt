package com.linc.inphoto.ui.postcomments.item

import android.text.method.LinkMovementMethod
import android.view.View
import com.linc.inphoto.R
import com.linc.inphoto.databinding.ItemPostCommentBinding
import com.linc.inphoto.ui.postcomments.model.CommentUiState
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.addModifiedSpans
import com.linc.inphoto.utils.extensions.bold
import com.linc.inphoto.utils.extensions.clickable
import com.linc.inphoto.utils.extensions.pattern.FORMAT_USER_COMMENT
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.xwray.groupie.viewbinding.BindableItem
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
                movementMethod = LinkMovementMethod.getInstance()
                text = String.format(
                    Locale.US,
                    FORMAT_USER_COMMENT,
                    commentUiState.username,
                    commentUiState.comment
                ).addModifiedSpans(listOf(commentUiState.username)) { spannable ->
                    spannable.bold().clickable(action = commentUiState.onUserClicked)
                }
            }
            dateTextView.text = DateFormatter.getRelativeTimeSpanString(
                root.context,
                commentUiState.createdTimestamp
            )
        }
    }

    override fun getLayout(): Int = R.layout.item_post_comment

    override fun initializeViewBinding(view: View) = ItemPostCommentBinding.bind(view)

}