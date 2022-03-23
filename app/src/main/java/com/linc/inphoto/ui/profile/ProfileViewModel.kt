package com.linc.inphoto.ui.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.MediaRepository
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.managepost.ManageablePost
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.profile.item.NewPostUiState
import com.linc.inphoto.ui.profile.model.ImageSource
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    router: Router,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val mediaRepository: MediaRepository
) : BaseViewModel<ProfileUiState>(router) {

    companion object {
        private const val EDIT_IMAGE_RESULT = "edit_image"
        private const val SELECT_IMAGE_RESULT = "select_image"
        private const val SELECT_SOURCE_RESULT = "select_source"
    }

    override val _uiState = MutableStateFlow(ProfileUiState())

    fun loadProfileData() = viewModelScope.launch {
        try {
            val user = userRepository.getLoggedInUser()
            val userPosts = postRepository.getCurrentUserPosts()
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

    fun updateProfileAvatar() {
        router.setResultListener(SELECT_SOURCE_RESULT) { result ->
            val selectedSource = result.safeCast<ImageSource>() ?: return@setResultListener
            selectImage(selectedSource, ::handleSelectedAvatar)
        }
        val pickerScreen = Navigation.ChooseOptionScreen(
            SELECT_SOURCE_RESULT, ImageSource.getAvailableSources()
        )
        router.navigateTo(pickerScreen)
    }

    private fun selectImage(
        imageSource: ImageSource,
        onSelected: (imageUri: Uri?) -> Unit
    ) {
        // Navigate to source screen
        val screen = when (imageSource) {
            is ImageSource.Gallery -> Navigation.GalleryScreen(SELECT_IMAGE_RESULT)
            is ImageSource.Camera -> Navigation.CameraScreen(SELECT_IMAGE_RESULT)
        }
        router.navigateTo(screen)

        router.setResultListener(SELECT_IMAGE_RESULT) {
            val editorScreen = Navigation.EditImageScreen(
                EDIT_IMAGE_RESULT,
                it as Uri
            )
            router.navigateTo(editorScreen)
        }

        router.setResultListener(EDIT_IMAGE_RESULT) {
            onSelected(it as? Uri)
        }
    }

    private fun handleSelectedAvatar(imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val user = userRepository.updateUserAvatar(imageUri)
                _uiState.update { copy(user = user) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun selectPost(post: Post) {
        val username = uiState.value.user?.name.orEmpty()
        val overviewType = OverviewType.Profile(post.id, post.authorUserId, username)
        router.navigateTo(Navigation.PostOverviewScreen(overviewType))
    }

    private fun createPost() {
        router.setResultListener(SELECT_SOURCE_RESULT) { result ->
            val selectedSource = result.safeCast<ImageSource>() ?: return@setResultListener
            selectImage(selectedSource) { imageUri ->
                imageUri ?: return@selectImage
                router.navigateTo(Navigation.ManagePostScreen(ManageablePost(imageUri)))
            }
        }
        val pickerScreen = Navigation.ChooseOptionScreen(
            SELECT_SOURCE_RESULT, ImageSource.getAvailableSources()
        )
        router.navigateTo(pickerScreen)
    }

}