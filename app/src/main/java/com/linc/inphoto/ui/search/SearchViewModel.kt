package com.linc.inphoto.ui.search

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.TagRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.entity.post.Tag
import com.linc.inphoto.entity.user.User
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.search.model.SearchTagUiState
import com.linc.inphoto.ui.search.model.SearchUserUiState
import com.linc.inphoto.ui.search.model.toUiState
import com.linc.inphoto.utils.extensions.mapIf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) : BaseViewModel<SearchUiState>(navContainerHolder) {

    /**
     *
     * (un) follow user
     * show tags
     * send message from profile screen
     *
     * */

    override val _uiState = MutableStateFlow(SearchUiState())

    fun selectPage(index: Int) {
        _uiState.update { it.copy(selectedPage = index) }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            try {
                launch { loadTags(query) }
                launch { loadUsers(query) }
                _uiState.update { it.copy(searchQuery = query) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private suspend fun loadUsers(query: String) {
        if (query.isEmpty()) {
            _uiState.update { it.copy(users = emptyList()) }
            return
        }
        val users = userRepository.loadUsers(query)
            .map(::getUserUiState)
            .sortedByDescending { it.isFollowing }
        _uiState.update { it.copy(users = users) }
    }

    private suspend fun loadTags(query: String) {
        if (query.isEmpty()) {
            _uiState.update { it.copy(tags = emptyList()) }
            return
        }
        val tags = tagRepository.loadTags(query)
            .map(::getTagUiState)
        _uiState.update { it.copy(tags = tags) }
    }

    private fun selectTag(tag: Tag) {
//        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

    private fun selectUser(user: User) {
        router.navigateTo(NavScreen.ProfileScreen(user.id))
    }

    private fun followUser(user: User) {
        viewModelScope.launch {
            try {
                val updatedUser = when {
                    user.isFollowingUser -> userRepository.followUser(user.id)
                    else -> userRepository.unfollowUser(user.id)
                } ?: return@launch
                val users = currentState.users.mapIf(
                    condition = { it.userId == user.id },
                    transform = { it.copy(isFollowing = updatedUser.isFollowingUser) }
                ).sortedByDescending { it.isFollowing }
                _uiState.update { it.copy(users = users) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun getUserUiState(user: User): SearchUserUiState {
        return user.toUiState(
            onClick = { selectUser(user) },
            onFollow = { followUser(user) }
        )
    }

    private fun getTagUiState(tag: Tag): SearchTagUiState {
        return tag.toUiState { selectTag(tag) }
    }

}