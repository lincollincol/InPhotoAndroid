package com.linc.inphoto.ui.postcomments

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postcomments.model.toUiState
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PostCommentsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val postRepository: PostRepository
) : BaseViewModel<PostCommentsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(PostCommentsUiState())

    fun loadPostComments(postId: String?) {
        viewModelScope.launch {
            try {
                postId ?: return@launch
                val post = postRepository.getExtendedPost(postId)
                val comments = postRepository.getPostComments(postId)
                    .map { comment ->
                        comment.toUiState { openProfile(comment.userId) }
                    }
                _uiState.update {
                    copy(postInfoUiState = post?.toUiState(), comments = comments)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun openProfile(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

}