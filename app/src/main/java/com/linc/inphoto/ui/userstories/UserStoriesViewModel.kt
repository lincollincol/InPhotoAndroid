package com.linc.inphoto.ui.userstories

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.StoryRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.storiesoverview.model.StoryTurnType
import com.linc.inphoto.utils.extensions.isValidIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserStoriesViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val storyRepository: StoryRepository
) : BaseViewModel<UserStoriesUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(UserStoriesUiState())

    fun loadUserStories(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val userStories = storyRepository.loadUserStories(userId) ?: return@launch
                _uiState.update { state ->
                    state.copy(
                        userId = userStories.userId,
                        username = userStories.username,
                        userAvatarUrl = userStories.userAvatarUrl,
                        stories = userStories.stories.sortedByDescending { it.createdTimestamp }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun selectProfile() {
        router.replaceScreen(NavScreen.ProfileScreen(currentState.userId))
    }

    fun selectStoryStep(step: Int) {
        if (!currentState.isStoriesLoaded) {
            return
        }
        if (!currentState.stories.isValidIndex(step)) {
            val turn = when {
                step >= currentState.stories.count() -> StoryTurnType.NEXT_STORY
                else -> StoryTurnType.PREVIOUS_STORY
            }
            _uiState.update { it.copy(storyTurn = turn) }
            return
        }
        _uiState.update { it.copy(storyPosition = step) }
    }

    fun previousStoryStep() = selectStoryStep(currentState.storyPosition - 1)

    fun nextStoryStep() = selectStoryStep(currentState.storyPosition + 1)

    fun storiesOverviewFinished() {
        _uiState.update { it.copy(storyTurn = StoryTurnType.NEXT_STORY) }
    }

    fun storyTurnShown() {
        _uiState.update { it.copy(storyTurn = null) }
    }

}