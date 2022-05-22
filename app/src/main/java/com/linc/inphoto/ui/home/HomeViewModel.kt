package com.linc.inphoto.ui.home

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.home.model.HomePostOperation
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.ui.home.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.ResourceProvider
import com.linc.inphoto.utils.extensions.mapIf
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
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<HomeUiState>(navContainerHolder) {

    companion object {
        private const val POST_ACTION_RESULT = "post_action_result"
    }

    override val _uiState = MutableStateFlow(HomeUiState())

    fun loadHomeData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                launch { loadCurrentUser() }
                launch { loadFollowingStories() }
                launch { loadFollowingPosts() }.join()
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadCurrentUser() {
        val newStoryState = userRepository.getLoggedInUser()
            ?.toUiState { }
        _uiState.update { it.copy(newStory = newStoryState) }
    }

    private suspend fun loadFollowingStories() {
//        val newStoryState = userRepository.getLoggedInUser()
//            ?.toUiState {  }
//        _uiState.update { it.copy(newStory = newStoryState) }
    }

    private suspend fun loadFollowingPosts() {
        val posts = postRepository.getCurrentUserFollowingExtendedPosts()
            .sortedByDescending { it.createdTimestamp }
            .map(::getHomePostUiState)
        _uiState.update { it.copy(posts = posts) }
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
                        transform = { getHomePostUiState(post) }
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
                        transform = { getHomePostUiState(post) }
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

    private fun selectUser(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

    private fun getHomePostUiState(post: ExtendedPost): HomePostUiState {
        return post.toUiState(
            onProfile = { selectUser(post.authorUserId) },
            onMore = { handlePostMenu(post) },
            onDoubleTap = { if (!post.isLiked) likePost(post) },
            onLike = { likePost(post) },
            onBookmark = { bookmarkPost(post) },
            onComment = { commentPost(post) },
        )
    }
}