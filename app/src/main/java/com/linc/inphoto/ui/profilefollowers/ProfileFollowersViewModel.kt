package com.linc.inphoto.ui.profilefollowers

import androidx.lifecycle.viewModelScope
import com.linc.inphoto.data.repository.UserRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.ui.profilefollowers.model.toUiState
import com.linc.inphoto.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileFollowersViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder,
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel<ProfileFollowersUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(ProfileFollowersUiState())

    fun loadUserSubscriptions(userId: String?) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserById(userId)
                val followers = userRepository.loadUserFollowers(userId)
                    .sortedBy { it.name }
                    .map { it.toUiState { selectUser(it.id) } }
                val following = userRepository.loadUserFollowing(userId)
                    .sortedBy { it.name }
                    .map { it.toUiState { selectUser(it.id) } }
                _uiState.update {
                    it.copy(
                        user = user,
                        followers = followers,
                        following = following
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun selectUser(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }
}