package com.linc.inphoto.ui.profile

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.main.MenuTab
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.model.ImageSource
import com.linc.inphoto.ui.profile.model.NewPostUiState
import com.linc.inphoto.ui.profile.model.ProfilePostUiState
import com.linc.inphoto.ui.profile.model.toUiState
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
    private val postRepository: PostRepository,
    private val mediaRepository: MediaRepository
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
            _uiState.update {
                it.copy(isProfileTab = containerId.equals(MenuTab.PROFILE.name, true))
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun openSettings() {
        router.navigateTo(NavScreen.SettingsScreen())
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
        val username = uiState.value.username.orEmpty()
        val overviewType = OverviewType.Profile(post.id, post.authorUserId, username)
        router.navigateTo(NavScreen.PostOverviewScreen(overviewType))
    }

    private suspend fun loadCurrentProfile() {
        val user = userRepository.getLoggedInUser()
        val userPosts = postRepository.getCurrentUserPosts()
            .sortedBy { it.createdTimestamp }
            .map { it.toUiState { selectPost(it) } }
        updateUserState(user, userPosts)
    }

    private suspend fun loadUserProfile(userId: String) {
        val user = userRepository.getUserById(userId)
        val userPosts = postRepository.getUserPosts(userId)
            .sortedBy { it.createdTimestamp }
            .map { it.toUiState { selectPost(it) } }
        updateUserState(user, userPosts)
    }

    private fun updateUserState(user: User?, posts: List<ProfilePostUiState>) {
        _uiState.update {
            it.copy(
                username = user?.name,
                status = user?.status,
                avatarUrl = user?.avatarUrl,
                headerUrl = user?.headerUrl,
                followersCount = 0,
                followingCount = 0,
                isLoggedInUser = user?.isLoggedInUser ?: false,
                posts = posts,
                newPostUiState = when (user?.isLoggedInUser) {
                    true -> NewPostUiState(::createPost)
                    else -> null
                }
            )
        }
    }
}