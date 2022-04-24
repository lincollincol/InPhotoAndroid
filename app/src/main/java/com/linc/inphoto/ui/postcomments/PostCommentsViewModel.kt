package com.linc.inphoto.ui.postcomments

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.postcomments.model.CommentUiState
import com.linc.inphoto.ui.postcomments.model.PostInfoUiState
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PostCommentsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<PostCommentsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(PostCommentsUiState())

    fun loadPostComments() {
        val postInfoUiState = PostInfoUiState(
            System.currentTimeMillis(),
            "Description",
            "XLINC",
            "https://firebase.google.com/downloads/brand-guidelines/PNG/logo-vertical.png",
            listOf("NZ", "tag", "city", "firebase")
        )
        val comments = mutableListOf<CommentUiState>()
        repeat(50) {
            comments += CommentUiState(
                UUID.randomUUID().toString(),
                System.currentTimeMillis() - Random.nextLong(1000, 10000000000),
                "Comment $it",
                "User $it",
                "https://firebase.google.com/downloads/brand-guidelines/PNG/logo-vertical.png"
            ) {
                Timber.d(it.toString())
            }
        }
        _uiState.update { copy(postInfoUiState = postInfoUiState, comments = comments) }
    }

}