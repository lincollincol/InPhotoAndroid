package com.linc.inphoto.ui.search

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.TagRepository
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.search.model.toUiState
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
        val users = userRepository.loadUsers(query)
            .map { it.toUiState { selectUser(it.id) } }
        _uiState.update { it.copy(users = users) }
    }

    private suspend fun loadTags(query: String) {
        val tags = tagRepository.loadTags(query)
            .map { it.toUiState { selectTag(it.id) } }
        _uiState.update { it.copy(tags = tags) }
    }

    private fun selectUser(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

    private fun selectTag(tagId: String) {
//        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

}