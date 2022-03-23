package com.linc.inphoto.ui.postsoverview

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.utils.ResourceProvider
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostOverviewViewModel @Inject constructor(
    router: Router,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<PostOverviewUiState>(router) {

    override val _uiState = MutableStateFlow(PostOverviewUiState())

    fun loadPosts(overviewType: OverviewType?) {
        viewModelScope.launch {
            try {
                when (overviewType) {
                    is OverviewType.Profile -> loadUserPosts(overviewType)
                    is OverviewType.Feed -> loadPublicPosts(overviewType)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private suspend fun loadUserPosts(profileOverview: OverviewType.Profile) = coroutineScope {
        _uiState.update { copy(posts = posts, postsSource = profileOverview.username) }
        val posts = postRepository.getUserExtendedPosts(profileOverview.userId).map { post ->
            post.toUiState(
                onDoubleTap = { if (!post.isLiked) likePost(post.id, post.isLiked) },
                onLike = { likePost(post.id, post.isLiked) },
                onBookmark = { bookmarkPost(post.id, post.isBookmarked) },
                onComment = { commentPost(post.id) },
            )
        }
        _uiState.update { copy(posts = posts) }
    }

    private suspend fun loadPublicPosts(feedOverview: OverviewType.Feed) = coroutineScope {
        val selectedPost = postRepository.getExtendedPost(feedOverview.postId)
        val recommendedPosts = postRepository.getExtendedPosts()
        val posts = listOfNotNull(selectedPost, *recommendedPosts.toTypedArray()).map { post ->
            post.toUiState(
                onDoubleTap = { if (!post.isLiked) likePost(post.id, post.isLiked) },
                onLike = { likePost(post.id, post.isLiked) },
                onBookmark = { bookmarkPost(post.id, post.isBookmarked) },
                onComment = { commentPost(post.id) },
            )
        }
        val postsSource = resourceProvider.getString(R.string.publications_toolbar)
        _uiState.update { copy(posts = posts, postsSource = postsSource) }
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