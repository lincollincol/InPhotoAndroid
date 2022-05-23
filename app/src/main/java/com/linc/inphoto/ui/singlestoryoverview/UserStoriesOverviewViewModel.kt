package com.linc.inphoto.ui.singlestoryoverview

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.StoryRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.storiesoverview.model.StoryTurnType
import com.linc.inphoto.utils.extensions.isValidIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserStoriesOverviewViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val storyRepository: StoryRepository
) : BaseViewModel<UserStoriesOverviewUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(UserStoriesOverviewUiState())

    fun storyTurnShown() {
        _uiState.update { it.copy(storyTurn = null) }
    }

    fun loadUserStories(userId: String) {
        viewModelScope.launch {
            try {
                val stories = storyRepository.loadUserStories(userId)
                _uiState.update { it.copy(userStories = stories) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun selectStoryStep(step: Int) {
        val stories = currentState.userStories?.stories.orEmpty()
        if (!stories.isValidIndex(step)) {
            val turn = when {
                step >= stories.count() -> StoryTurnType.NEXT_STORY
                else -> StoryTurnType.PREVIOUS_STORY
            }
            _uiState.update { it.copy(storyTurn = turn) }
            return
        }
        _uiState.update { it.copy(storyPosition = step) }
    }

    fun previousStoryStep() {
        selectStoryStep(currentState.storyPosition - 1)
    }

    fun nextStoryStep() {
        selectStoryStep(currentState.storyPosition + 1)
    }

    fun storiesOverviewFinished() {
        _uiState.update { it.copy(storyTurn = StoryTurnType.NEXT_STORY) }
    }

}