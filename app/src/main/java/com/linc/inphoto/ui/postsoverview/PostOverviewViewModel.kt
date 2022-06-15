package com.linc.inphoto.ui.postsoverview

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.managepost.model.ManagePostIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.postsoverview.model.PostOperation
import com.linc.inphoto.utils.ResourceProvider
import com.linc.inphoto.utils.extensions.mapIf
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
    private val postRepository: PostRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<PostOverviewUiState>(navContainerHolder) {

    companion object {
        private const val POST_ACTION_RESULT = "post_action_result"
    }

    override val _uiState = MutableStateFlow(PostOverviewUiState())
    private var overviewType: OverviewType? = null

    fun initialPostShown() {
        _uiState.update { it.copy(initialPosition = null) }
    }

    fun applyOverviewType(overviewType: OverviewType?) {
        this.overviewType = overviewType
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                loadPosts()
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadPosts() = coroutineScope {
        val type = overviewType ?: return@coroutineScope
        val selectedPost = postRepository.getExtendedPost(type.postId) ?: return@coroutineScope
        val posts = when (type) {
            is OverviewType.Profile -> postRepository.getUserExtendedPosts(type.userId)
                .sortedByDescending { it.createdTimestamp }
            is OverviewType.TagPosts -> postRepository.getExtendedTagPosts(type.tagId)
                .sortedByDescending { it.createdTimestamp }
            is OverviewType.Feed ->
                listOfNotNull(selectedPost, *postRepository.getAllExtendedPosts().toTypedArray())
        }.map(::getPostUiState)
        val initialPosition = posts.indexOfFirst { it.postId == selectedPost.id }
        _uiState.update { it.copy(posts = posts, initialPosition = initialPosition) }
    }

    private fun likePost(selectedPost: ExtendedPost) {
        viewModelScope.launch {
            try {
                val post = postRepository.likePost(
                    selectedPost.id,
                    !selectedPost.isLiked
                ) ?: return@launch
                val posts = currentState.posts
                    .sortedByDescending { it.createdTimestamp }
                    .mapIf(
                        condition = { it.postId == selectedPost.id },
                        transform = { getPostUiState(post) }
                    )
                _uiState.update { it.copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun bookmarkPost(selectedPost: ExtendedPost) {
        viewModelScope.launch {
            try {
                val post = postRepository.bookmarkPost(
                    selectedPost.id,
                    !selectedPost.isBookmarked
                ) ?: return@launch
                val posts = currentState.posts
                    .sortedByDescending { it.createdTimestamp }
                    .mapIf(
                        condition = { it.postId == selectedPost.id },
                        transform = { getPostUiState(post) }
                    )
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
        val postOperations = when {
            selectedPost.isCurrentUserAuthor -> PostOperation.getAuthorPostOperations()
            else -> PostOperation.getGuestPostOperations()
        }
        router.showDialog(NavScreen.ChooseOptionScreen(POST_ACTION_RESULT, postOperations))
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

    private fun selectUser(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

    private fun selectImage(post: ExtendedPost) {
        val imagesToOverview = listOf(post.contentUrl.toUri())
        router.navigateTo(NavScreen.MediaReviewScreen(imagesToOverview))
    }

    private fun getPostUiState(post: ExtendedPost): PostUiState {
        return post.toUiState(
            onImage = { selectImage(post) },
            onProfile = { selectUser(post.authorUserId) },
            onMore = { handlePostMenu(post) },
            onDoubleTap = { if (!post.isLiked) likePost(post) },
            onLike = { likePost(post) },
            onBookmark = { bookmarkPost(post) },
            onComment = { commentPost(post) },
        )
    }

}