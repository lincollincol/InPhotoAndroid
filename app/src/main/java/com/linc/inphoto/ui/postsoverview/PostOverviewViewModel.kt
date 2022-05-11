package com.linc.inphoto.ui.postsoverview

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.managepost.model.ManagePostIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.postsoverview.model.PostOperation
import com.linc.inphoto.utils.ResourceProvider
import com.linc.inphoto.utils.extensions.safeCast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostOverviewViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<PostOverviewUiState>(navContainerHolder) {

    companion object {
        private const val POST_ACTION_RESULT = "post_action_result"
    }

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
        _uiState.update { it.copy(postsSource = profileOverview.username) }
        val posts = postRepository.getUserExtendedPosts(profileOverview.userId)
            .sortedBy { it.createdTimestamp }
            .map { post ->
                post.toUiState(
                    onMore = { handlePostMenu(post) },
                    onDoubleTap = { if (!post.isLiked) likePost(post) },
                    onLike = { likePost(post) },
                    onBookmark = { bookmarkPost(post) },
                    onComment = { commentPost(post) },
                )
            }
        _uiState.update { it.copy(posts = posts) }
    }

    private suspend fun loadPublicPosts(feedOverview: OverviewType.Feed) = coroutineScope {
        val selectedPost = postRepository.getExtendedPost(feedOverview.postId)
        val recommendedPosts = postRepository.getExtendedPosts()
        val posts = listOfNotNull(selectedPost, *recommendedPosts.toTypedArray()).map { post ->
            post.toUiState(
                onMore = { handlePostMenu(post) },
                onDoubleTap = { if (!post.isLiked) likePost(post) },
                onLike = { likePost(post) },
                onBookmark = { bookmarkPost(post) },
                onComment = { commentPost(post) },
            )
        }
        val postsSource = resourceProvider.getString(R.string.publications_toolbar)
        _uiState.update { it.copy(posts = posts, postsSource = postsSource) }
    }

    // TODO: 23.03.22 refactor code. Move ui state mapping to new function?
    private fun likePost(selectedPost: ExtendedPost) {
        viewModelScope.launch {
            try {
                val post =
                    postRepository.likePost(selectedPost.id, !selectedPost.isLiked) ?: return@launch
                val posts = currentState.posts
                    .sortedBy { it.createdTimestamp }
                    .map { postUiState ->
                        when (postUiState.postId) {
                            selectedPost.id -> post.toUiState(
                                onMore = { handlePostMenu(selectedPost) },
                                onDoubleTap = { if (!post.isLiked) likePost(post) },
                                onLike = { likePost(post) },
                                onBookmark = { bookmarkPost(post) },
                                onComment = { commentPost(post) },
                            )
                            else -> postUiState
                        }
                    }
                _uiState.update { it.copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun bookmarkPost(selectedPost: ExtendedPost) {
        viewModelScope.launch {
            try {
                val post = postRepository.bookmarkPost(selectedPost.id, !selectedPost.isBookmarked)
                    ?: return@launch
                val posts = currentState.posts
                    .sortedBy { it.createdTimestamp }
                    .map { postUiState ->
                        when (postUiState.postId) {
                            selectedPost.id -> post.toUiState(
                                onMore = { handlePostMenu(selectedPost) },
                                onDoubleTap = { if (!post.isLiked) likePost(post) },
                                onLike = { likePost(post) },
                                onBookmark = { bookmarkPost(post) },
                                onComment = { commentPost(post) },
                            )
                            else -> postUiState
                        }
                    }
                _uiState.update { it.copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun commentPost(selectedPost: ExtendedPost) {
        router.navigateTo(NavScreen.PostCommentsScreen(selectedPost.id))
    }

    private fun handlePostMenu(selectedPost: ExtendedPost) {
        router.setResultListener(POST_ACTION_RESULT) { result ->
            val operation = result.safeCast<PostOperation>() ?: return@setResultListener
            when (operation) {
                PostOperation.Edit -> editPost(selectedPost)
                PostOperation.Delete -> deletePost(selectedPost)
                PostOperation.Share -> sharePost(selectedPost)
            }
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            POST_ACTION_RESULT, PostOperation.getPostOperations()
        )
        router.showDialog(pickerScreen)
    }

    private fun editPost(selectedPost: ExtendedPost) {
        val intent = ManagePostIntent.EditPost(
            selectedPost.id,
            selectedPost.contentUrl,
            selectedPost.description,
            selectedPost.tags
        )
        router.navigateTo(NavScreen.ManagePostScreen(intent))
    }

    private fun deletePost(selectedPost: ExtendedPost) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(selectedPost.id)
                val posts = currentState.posts.mapNotNull { postUiState ->
                    when (postUiState.postId) {
                        selectedPost.id -> null
                        else -> postUiState
                    }
                }
                _uiState.update { it.copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun sharePost(selectedPost: ExtendedPost) {
        val content = resourceProvider.getString(
            R.string.share_post_template,
            selectedPost.username,
            selectedPost.description,
            selectedPost.tags.toString(),
            selectedPost.contentUrl
        )
        router.navigateTo(NavScreen.ShareContentScreen(content))
    }

}