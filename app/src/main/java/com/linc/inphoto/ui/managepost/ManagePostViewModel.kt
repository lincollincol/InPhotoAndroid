package com.linc.inphoto.ui.managepost

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.managepost.model.ManagePostIntent
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManagePostViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val postRepository: PostRepository
) : BaseViewModel<ManagePostUiState>(navContainerHolder) {

    companion object {
        private const val INFO_RESULT = "info_result"
    }

    override val _uiState = MutableStateFlow(ManagePostUiState())
    private var intent: ManagePostIntent? = null

    fun applyPost(intent: ManagePostIntent) {
        this.intent = intent
        _uiState.update {
            when (intent) {
                is ManagePostIntent.NewPost -> copy(imageUri = intent.imageUri)
                is ManagePostIntent.EditPost -> copy(
                    postId = intent.postId,
                    imageUri = intent.contentUrl.toUri(),
                    description = intent.description,
                    tags = intent.tags.toSet()
                )
            }
        }
    }

    fun addTags(tag: String) {
        val tags = uiState.value.tags.toTypedArray()
        _uiState.update { copy(tags = setOf(*tags, tag)) }
    }

    fun removeTags(tag: String) {
        val tags = uiState.value.tags.toMutableSet()
        tags.remove(tag)
        _uiState.update { copy(tags = tags) }
    }

    fun updateDescription(description: String) {
        _uiState.update { copy(description = description) }
    }

    fun savePost() {
        viewModelScope.launch {
            try {
//                _uiState.update { copy(isLoading = true) }
                val state = uiState.value
                if (!state.isValidPostData) {
                    _uiState.update { copy(isErrorsEnabled = true) }
                    return@launch
                }

                if (intent is ManagePostIntent.NewPost) {
                    createPost()
                    router.backTo(NavScreen.ProfileScreen())
                } else {
                    updatePost()
                    router.exit()
                }

            } catch (e: Exception) {
                Timber.e(e)
            } finally {
//                _uiState.update { copy(isLoading = false) }
            }
        }
    }

    suspend fun createPost() {
        val state = uiState.value
        postRepository.saveUserPost(
            state.imageUri,
            state.description.orEmpty(),
            state.tags.toList(),
        )
    }

    suspend fun updatePost() {
        val state = uiState.value
        postRepository.updateUserPost(
            state.postId.orEmpty(),
            state.description.orEmpty(),
            state.tags.toList()
        )
    }

    fun cancelPost() {
        router.exit()
    }

}