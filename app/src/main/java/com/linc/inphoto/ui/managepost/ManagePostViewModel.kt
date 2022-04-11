package com.linc.inphoto.ui.managepost

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.managepost.model.ManageablePost
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

    fun applyPost(post: ManageablePost) {
        _uiState.update {
            copy(
                postId = post.id,
                imageUri = post.imageUri,
                description = post.description,
                tags = post.tags.toSet()
            )
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

                if (state.postId.isNullOrEmpty()) {
                    postRepository.saveUserPost(
                        state.imageUri,
                        state.description.orEmpty(),
                        state.tags.toList(),
                    )
                } else {
                    postRepository.updateUserPost(
                        state.postId,
                        state.description.orEmpty(),
                        state.tags.toList()
                    )
                }

                router.backTo(NavScreen.ProfileScreen())
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
//                _uiState.update { copy(isLoading = false) }
            }
        }
    }

    fun cancelPost() {
        router.exit()
    }

}