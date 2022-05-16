package com.linc.inphoto.ui.home

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.home.model.HomePostOperation
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.ui.home.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.ResourceProvider
import com.linc.inphoto.utils.extensions.safeCast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val postRepository: PostRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<HomeUiState>(navContainerHolder) {

    /**
     * TODO:
     * current user posts bug
     * ??? maybe stories for home screen ???
     * open profile from post
     */

    companion object {
        private const val POST_ACTION_RESULT = "post_action_result"
    }

    override val _uiState = MutableStateFlow(HomeUiState())

    fun loadFollowingPosts() {
        viewModelScope.launch {
            try {
                val posts = postRepository.getCurrentUserFollowingExtendedPosts()
                    .sortedByDescending { it.createdTimestamp }
                    .map(::getHomePostUiState)
                _uiState.update { it.copy(posts = posts) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    // TODO: 23.03.22 refactor code. Move ui state mapping to new function?
    private fun likePost(selectedPost: ExtendedPost) {
        viewModelScope.launch {
            try {
                val post = postRepository.likePost(
                    selectedPost.id,
                    !selectedPost.isLiked
                ) ?: return@launch
                val posts = currentState.posts
                    .sortedByDescending { it.createdTimestamp }
                    .map { postUiState ->
                        when (postUiState.postId) {
                            selectedPost.id -> getHomePostUiState(post)
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
                val post = postRepository.bookmarkPost(
                    selectedPost.id,
                    !selectedPost.isBookmarked
                ) ?: return@launch
                val posts = currentState.posts
                    .sortedByDescending { it.createdTimestamp }
                    .map { postUiState ->
                        when (postUiState.postId) {
                            selectedPost.id -> getHomePostUiState(post)
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
            val operation = result.safeCast<HomePostOperation>() ?: return@setResultListener
            when (operation) {
                HomePostOperation.Share -> sharePost(selectedPost)
                HomePostOperation.Report -> return@setResultListener
            }
        }
        router.showDialog(
            NavScreen.ChooseOptionScreen(POST_ACTION_RESULT, HomePostOperation.getPostOperations())
        )
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

    private fun getHomePostUiState(post: ExtendedPost): HomePostUiState {
        return post.toUiState(
            onMore = { handlePostMenu(post) },
            onDoubleTap = { if (!post.isLiked) likePost(post) },
            onLike = { likePost(post) },
            onBookmark = { bookmarkPost(post) },
            onComment = { commentPost(post) },
        )
    }
}