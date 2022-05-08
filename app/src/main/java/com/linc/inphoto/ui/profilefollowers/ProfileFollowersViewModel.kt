package com.linc.inphoto.ui.profilefollowers

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.profilefollowers.model.FollowerUserUiState
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.ui.profilefollowers.model.toUiState
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileFollowersViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository
) : BaseViewModel<ProfileFollowersUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ProfileFollowersUiState())
    private val followers = mutableListOf<FollowerUserUiState>()
    private val following = mutableListOf<FollowerUserUiState>()

    fun loadUserSubscriptions(
        userId: String?,
        subscriptionType: SubscriptionType?
    ) {
        _uiState.update { it.copy(selectedPage = subscriptionType?.ordinal ?: 0) }
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                _uiState.update { it.copy(user = user) }
                loadFollowers(userId)
                loadFollowing(userId)
                applySearchQuery()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun selectPage(index: Int) {
        _uiState.update { it.copy(selectedPage = index) }
    }

    fun updateSearchQuery(query: String?) {
        _uiState.update { it.copy(searchQuery = query) }
        applySearchQuery()
    }

    private fun applySearchQuery() {
        val query = currentState.searchQuery.orEmpty()
        _uiState.update { state ->
            state.copy(
                followers = followers.filter { it.username.contains(query) },
                following = following.filter { it.username.contains(query) }
            )
        }
    }

    private suspend fun loadFollowers(userId: String?) {
        userRepository.loadUserFollowers(userId)
            .sortedBy { it.name }
            .map { it.toUiState { selectUser(it.id) } }
            .also { followers.update(it) }
    }

    private suspend fun loadFollowing(userId: String?) {
        userRepository.loadUserFollowing(userId)
            .sortedBy { it.name }
            .map { it.toUiState { selectUser(it.id) } }
            .also { following.update(it) }
    }

    private fun selectUser(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }
}