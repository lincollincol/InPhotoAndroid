package com.linc.inphoto.ui.followerslist

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.followerslist.model.SubscriptionType
import com.linc.inphoto.ui.followerslist.model.toUiState
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FollowersListViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository
) : BaseViewModel<FollowersListUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(FollowersListUiState())

    fun loadUsers(userId: String?, type: SubscriptionType?) {
        viewModelScope.launch {
            try {
                val users = when (type) {
                    SubscriptionType.FOLLOWER -> userRepository.loadUserFollowers(userId)
                    else -> userRepository.loadUserFollowing(userId)
                }.map { it.toUiState { selectUser(it.id) } }
                _uiState.update { it.copy(users = users) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun selectUser(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

}