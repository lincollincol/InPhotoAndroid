package com.linc.inphoto.ui.tagposts

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.PostRepository
import com.linc.inphoto.data.repository.TagRepository
import com.linc.inphoto.entity.post.Post
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.ui.tagposts.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TagPostsViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val tagRepository: TagRepository,
    private val postRepository: PostRepository
) : BaseViewModel<TagPostsUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(TagPostsUiState())
    private var tagId: String? = null

    fun loadTagPosts(tagId: String?) {
        this.tagId = tagId
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val posts = postRepository.getTagPosts(tagId)
                    .sortedByDescending { it.createdTimestamp }
                    .map { it.toUiState { selectPost(it) } }
                val tag = tagRepository.loadTag(tagId)
                _uiState.update {
                    it.copy(
                        name = tag?.name,
                        postPreviewUrl = posts.firstOrNull()?.postContentUrl,
                        postsCount = posts.count(),
                        posts = posts
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun selectPost(post: Post) {
        val overviewType = OverviewType.TagPosts(post.id, tagId.toString())
        router.navigateTo(NavScreen.PostOverviewScreen(overviewType))
    }

}