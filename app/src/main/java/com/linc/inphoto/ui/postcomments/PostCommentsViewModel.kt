package com.linc.inphoto.ui.postcomments

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postcomments.model.CommentAction
import com.linc.inphoto.ui.postcomments.model.toUiState
import com.linc.inphoto.utils.extensions.mapIf
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.toImmutableDeque
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PostCommentsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val postRepository: PostRepository
) : BaseViewModel<PostCommentsUiState>(navContainerHolder) {

    companion object {
        private const val COMMENT_ACTION_RESULT = "comment_action_result"
    }

    override val _uiState = MutableStateFlow(PostCommentsUiState())
    private var postId: String? = null

    fun updateCommentMessage(message: String?) {
        _uiState.update { it.copy(commentMessage = message) }
    }

    fun cancelCommentEditor() {
        _uiState.update { it.copy(editableCommentId = null, commentMessage = null) }
    }

    fun loadPostComments(postId: String?) {
        viewModelScope.launch {
            try {
                this@PostCommentsViewModel.postId = postId ?: return@launch
                val post = postRepository.getExtendedPost(postId)
                val comments = postRepository.getPostComments(postId)
                    .sortedByDescending { it.createdTimestamp }
                    .map { comment ->
                        comment.toUiState(
                            onUserClicked = { openProfile(comment.userId) },
                            onCommentClicked = { handleCommentMenu(comment.id) }
                        )
                    }
                _uiState.update {
                    it.copy(
                        postInfoUiState = post?.toUiState { openProfile(post.authorUserId) },
                        comments = comments.toImmutableDeque()
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun saveComment() {
        viewModelScope.launch {
            try {
                val comment = postRepository.commentPost(
                    postId.toString(),
                    currentState.commentMessage.toString()
                ) ?: return@launch
                val comments = currentState.comments.toMutableDeque()
                comments.addFirst(comment.toUiState(
                    onUserClicked = { openProfile(comment.userId) },
                    onCommentClicked = { handleCommentMenu(comment.id) }
                ))
                _uiState.update {
                    it.copy(
                        comments = comments.toImmutableDeque(),
                        commentMessage = null
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun updateComment() {
        viewModelScope.launch {
            try {
                postRepository.updatePostComment(
                    currentState.editableCommentId.toString(),
                    currentState.commentMessage.toString()
                )
                val comments = currentState.comments.toMutableDeque()
                    .mapIf({ it.commentId == currentState.editableCommentId }) { commentUiState ->
                        commentUiState.copy(comment = currentState.commentMessage.orEmpty())
                    }
                _uiState.update {
                    it.copy(
                        comments = comments.toImmutableDeque(),
                        editableCommentId = null,
                        commentMessage = null
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun handleCommentMenu(commentId: String) {
        router.setResultListener(COMMENT_ACTION_RESULT) { result ->
            when (result.safeCast<CommentAction>()) {
                CommentAction.Edit -> editComment(commentId)
                CommentAction.Delete -> deleteComment(commentId)
            }
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            COMMENT_ACTION_RESULT, CommentAction.getCommentActions()
        )
        router.showDialog(pickerScreen)
    }

    private fun deleteComment(commentId: String) {
        viewModelScope.launch {
            try {
                postRepository.deletePostComment(commentId)
                val comments = currentState.comments.toMutableDeque()
                comments.removeAll { it.commentId == commentId }
                _uiState.update { it.copy(comments = comments.toImmutableDeque()) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun editComment(commentId: String) {
        val comment = currentState.comments.find { it.commentId == commentId }
        _uiState.update {
            it.copy(editableCommentId = commentId, commentMessage = comment?.comment)
        }
    }

    private fun openProfile(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

}