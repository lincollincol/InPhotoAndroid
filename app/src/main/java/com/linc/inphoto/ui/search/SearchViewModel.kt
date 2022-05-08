package com.linc.inphoto.ui.search

import androidx.lifecycle.viewModelScope
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
    private val userRepository: UserRepository
) : BaseViewModel<SearchUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(SearchUiState())

    fun loadUsers() {
        viewModelScope.launch {
            try {
                val users = userRepository.loadAllUsers()
                    .map { it.toUiState { openProfile(it.id) } }
                _uiState.update { it.copy(users = users) }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun openProfile(userId: String) {
        router.navigateTo(NavScreen.ProfileScreen(userId))
    }

}