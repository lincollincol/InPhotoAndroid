package com.linc.inphoto.ui.home

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.StoryRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.ExtendedPost
import com.linc.inphoto.entity.story.UserStory
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.home.model.HomePostOperation
import com.linc.inphoto.ui.home.model.HomePostUiState
import com.linc.inphoto.ui.home.model.StoryContentSource
import com.linc.inphoto.ui.home.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
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
class HomeViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val storyRepository: StoryRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<HomeUiState>(navContainerHolder) {

    companion object {
        private const val POST_ACTION_RESULT = "post_action_result"
        private const val IMAGE_SOURCE_RESULT = "image_source"
    }

    override val _uiState = MutableStateFlow(HomeUiState())

    fun loadHomeData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                launch { loadCurrentUser() }
                launch { loadStories() }
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
            ?.toUiState { createUserStory() }
        val storiesState = currentState.storiesUiState.copy(newStory = newStoryState)
        _uiState.update { it.copy(storiesUiState = storiesState) }
    }

    private suspend fun loadStories() = coroutineScope {
        val stories = storyRepository.loadCurrentUserFollowingStories()
            .sortedByDescending { it.latestStoryTimestamp }
            .sortedByDescending { it.isLoggedInUser }
            .map { it.toUiState { selectUserStory(it) } }
        val storiesState = currentState.storiesUiState.copy(stories = stories)
        _uiState.update { it.copy(storiesUiState = storiesState) }
    }

    private suspend fun loadFollowingPosts() {
        val posts = postRepository.getCurrentUserFollowingExtendedPosts()
            .sortedByDescending { it.createdTimestamp }
            .map(::getHomePostUiState)
        _uiState.update { it.copy(posts = posts) }
    }

    private fun selectUserStory(userStory: UserStory) {
        router.navigateTo(NavScreen.StoriesOverviewScreen(userStory.userId))
    }

    private fun createUserStory() {
        router.setResultListener(IMAGE_SOURCE_RESULT) { result ->
            val screen = when (result.safeCast<StoryContentSource>()) {
                StoryContentSource.Camera -> NavScreen.CameraScreen(CameraIntent.NewStory)
                StoryContentSource.Gallery -> NavScreen.GalleryScreen(GalleryIntent.NewStory)
                else -> return@setResultListener
            }
            router.navigateTo(screen)
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            IMAGE_SOURCE_RESULT,
            StoryContentSource.getAvailableSources()
        )
        router.showDialog(pickerScreen)
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
        val postOperations = when {
            selectedPost.isCurrentUserAuthor -> HomePostOperation.getAuthorPostOperations()
            else -> HomePostOperation.getGuestPostOperations()
        }
        router.showDialog(NavScreen.ChooseOptionScreen(POST_ACTION_RESULT, postOperations))
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

    private fun getHomePostUiState(post: ExtendedPost): HomePostUiState {
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