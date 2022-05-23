package com.linc.inphoto.ui.storiesoverview

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.StoryRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.storiesoverview.model.StoryTurnType
import com.linc.inphoto.ui.storiesoverview.model.toUiState
import com.linc.inphoto.utils.extensions.isValidIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoriesOverviewViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val storyRepository: StoryRepository
) : BaseViewModel<StoriesOverviewUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(StoriesOverviewUiState())

    fun loadStories(initialUserId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val stories = storyRepository.loadCurrentUserFollowingStories()
                    .sortedByDescending { it.latestStoryTimestamp }
                    .map { it.toUiState() }
                val storyPosition = stories.indexOfFirst { it.userId == initialUserId }
                _uiState.update { it.copy(stories = stories, storyPosition = storyPosition) }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectStoryPage(position: Int) {
        _uiState.update { it.copy(storyPosition = position) }
    }

    fun selectStoryTurn(turn: StoryTurnType) {
        val newPosition = when (turn) {
            StoryTurnType.NEXT_STORY -> currentState.storyPosition + 1
            StoryTurnType.PREVIOUS_STORY -> currentState.storyPosition - 1
        }
        if (currentState.stories.isValidIndex(newPosition)) {
            _uiState.update { it.copy(storyPosition = newPosition) }
        } else {
            router.exit()
        }
    }


}