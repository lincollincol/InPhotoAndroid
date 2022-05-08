package com.linc.inphoto.ui.profile

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.model.ImageSource
import com.linc.inphoto.ui.profile.model.NewPostUiState
import com.linc.inphoto.ui.profile.model.toUiState
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.utils.extensions.safeCast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel<ProfileUiState>(navContainerHolder) {

    companion object {
        private const val IMAGE_SOURCE_RESULT = "image_source"
    }

    override val _uiState = MutableStateFlow(ProfileUiState())

    fun loadProfileData(userId: String?) = viewModelScope.launch {
        try {
            when {
                userId.isNullOrEmpty() -> loadCurrentProfile()
                else -> loadUserProfile(userId)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun followUser() {
        viewModelScope.launch {
            try {
                updateUserState(userRepository.followUser(currentState.user?.id))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun unfollowUser() {
        viewModelScope.launch {
            try {
                updateUserState(userRepository.unfollowUser(currentState.user?.id))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun openSettings() {
        router.navigateTo(NavScreen.SettingsScreen())
    }

    fun openFollowing() = openFollowers(currentState.user?.id, SubscriptionType.FOLLOWING)

    fun openFollowers() = openFollowers(currentState.user?.id, SubscriptionType.FOLLOWER)

    fun openFollowers(userId: String?, subscriptionType: SubscriptionType) {
        router.navigateTo(NavScreen.ProfileFollowersScreen(userId, subscriptionType))
    }

    private fun createPost() {
        selectImageSource { imageSource ->
            val screen = when (imageSource) {
                is ImageSource.Gallery -> NavScreen.GalleryScreen(GalleryIntent.NewPost)
                is ImageSource.Camera -> NavScreen.CameraScreen(CameraIntent.NewPost)
            }
            router.navigateTo(screen)
        }
    }

    private fun selectImageSource(onSelected: (ImageSource) -> Unit) {
        router.setResultListener(IMAGE_SOURCE_RESULT) { result ->
            val selectedSource = result.safeCast<ImageSource>() ?: return@setResultListener
            onSelected(selectedSource)
        }
        val pickerScreen = NavScreen.ChooseOptionScreen(
            IMAGE_SOURCE_RESULT,
            ImageSource.getAvailableSources()
        )
        router.showDialog(pickerScreen)
    }

    private fun selectPost(post: Post) {
        val username = currentState.user?.name.orEmpty()
        val overviewType = OverviewType.Profile(post.id, post.authorUserId, username)
        router.navigateTo(NavScreen.PostOverviewScreen(overviewType))
    }

    private suspend fun loadCurrentProfile() {
        val user = userRepository.getLoggedInUser()
        val userPosts = postRepository.getCurrentUserPosts()
            .sortedBy { it.createdTimestamp }
            .map { it.toUiState { selectPost(it) } }
        updateUserState(user)
        _uiState.update { it.copy(posts = userPosts) }
    }

    private suspend fun loadUserProfile(userId: String) {
        val user = userRepository.getUserById(userId)
        val userPosts = postRepository.getUserPosts(userId)
            .sortedBy { it.createdTimestamp }
            .map { it.toUiState { selectPost(it) } }
        updateUserState(user)
        _uiState.update { it.copy(posts = userPosts) }
    }

    private fun updateUserState(user: User?) {
        _uiState.update {
            it.copy(
                user = user,
                newPostUiState = when (user?.isLoggedInUser) {
                    true -> NewPostUiState(::createPost)
                    else -> null
                }
            )
        }
    }
}