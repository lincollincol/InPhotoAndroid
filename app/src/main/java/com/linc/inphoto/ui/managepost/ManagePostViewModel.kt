package com.linc.inphoto.ui.managepost

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManagePostViewModel @Inject constructor(
    router: Router,
    private val postRepository: PostRepository
) : BaseViewModel<ManagePostUiState>(router) {

    companion object {
        private const val INFO_RESULT = "info_result"
    }

    override val _uiState = MutableStateFlow(ManagePostUiState())

    fun applyPost(post: ManageablePost) {
        _uiState.update {
            copy(
                imageUri = post.imageUri,
                description = post.description,
                tags = post.tags
            )
        }
    }


    fun addTags(tag: String) {
        val tags = uiState.value.tags.toTypedArray()
        _uiState.update { copy(tags = listOf(*tags, tag)) }
    }

    fun removeTags(tag: String) {
        val tags = uiState.value.tags.toMutableList()
        tags.remove(tag)
        _uiState.update { copy(tags = tags) }
    }

    fun updateDescription(description: String) {
        _uiState.update { copy(description = description) }
    }

    fun savePost() {
        viewModelScope.launch {
            try {
                postRepository.updateUserAvatar(
                    uiState.value.imageUri,
                    uiState.value.description,
                    uiState.value.tags,
                )

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun cancelPost() {
        router.exit()
    }

}