package com.linc.inphoto.ui.profile

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.model.ImageSource
import com.linc.inphoto.ui.profile.model.NewPostUiState
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun loadProfileData() = viewModelScope.launch {
        try {
            val user = userRepository.getLoggedInUser()
            val userPosts = postRepository.getCurrentUserPosts()
                .sortedBy { it.createdTimestamp }
                .map { it.toUiState { selectPost(it) } }

            _uiState.update {
                copy(
                    user = user,
                    posts = userPosts,
                    newPostUiState = NewPostUiState(::createPost)
                )
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun updateAvatar() {
        selectImageSource { imageSource ->
            val screen = when (imageSource) {
                is ImageSource.Gallery -> NavScreen.GalleryScreen(GalleryIntent.NewAvatar)
                is ImageSource.Camera -> NavScreen.CameraScreen(CameraIntent.NewAvatar)
            }
            router.navigateTo(screen)
        }
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
        val username = uiState.value.user?.name.orEmpty()
        val overviewType = OverviewType.Profile(post.id, post.authorUserId, username)
        router.navigateTo(NavScreen.PostOverviewScreen(overviewType))
    }
}