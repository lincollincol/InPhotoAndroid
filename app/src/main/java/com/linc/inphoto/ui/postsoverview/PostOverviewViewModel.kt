package com.linc.inphoto.ui.postsoverview

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostOverviewViewModel @Inject constructor(
    router: Router,
    private val postRepository: PostRepository
) : BaseViewModel<PostOverviewUiState>(router) {

    override val _uiState = MutableStateFlow(PostOverviewUiState())

    fun loadPosts(overviewType: OverviewType?) {
        viewModelScope.launch {
            try {
                val posts = if (overviewType is OverviewType.Profile) {
                    postRepository.getUserExtendedPosts(overviewType.userId)
                } else {
                    emptyList<ExtendedPost>()
                }
                val postStates = posts.map { post ->
                    post.toUiState(
                        onDoubleTap = { if (!post.isLiked) likePost(post.id, post.isLiked) },
                        onLike = { likePost(post.id, post.isLiked) },
                        onBookmark = { bookmarkPost(post.id, post.isBookmarked) },
                        onComment = { commentPost(post.id) },
                    )
                }
                val initialPost = posts.find { it.id == overviewType?.postId }
                _uiState.update { copy(posts = postStates, initialPost = initialPost) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun likePost(postId: String, isLiked: Boolean) {
        viewModelScope.launch {
            try {
                val post = postRepository.likePost(postId, !isLiked) ?: return@launch
                val posts = uiState.value.posts.map { postUiState ->
                    when (postUiState.postId) {
                        postId -> post.toUiState(
                            onDoubleTap = { if (!post.isLiked) likePost(post.id, post.isLiked) },
                            onLike = { likePost(post.id, post.isLiked) },
                            onBookmark = { bookmarkPost(post.id, post.isBookmarked) },
                            onComment = { commentPost(post.id) },
                        )
                        else -> postUiState
                    }
                }
                _uiState.update { copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun bookmarkPost(postId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            try {
                val post = postRepository.bookmarkPost(postId, !isBookmarked) ?: return@launch
                val posts = uiState.value.posts.map {
                    if (it.postId == postId) {
                        post.toUiState(
                            onDoubleTap = { if (!post.isLiked) likePost(post.id, post.isLiked) },
                            onLike = { likePost(post.id, post.isLiked) },
                            onBookmark = { bookmarkPost(post.id, post.isBookmarked) },
                            onComment = { commentPost(post.id) },
                        )
                    } else {
                        it
                    }
                }
                _uiState.update { copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun commentPost(postId: String) {

    }

}