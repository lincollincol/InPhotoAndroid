package com.linc.inphoto.ui.profile

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.data.repository.UsersRepository
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val router: Router,
    private val usersRepository: UsersRepository
) : BaseViewModel<ProfileUiState, BaseUiEffect>(router) {

    fun getUserData() = launchCoroutine {
        val user = usersRepository.getLoggedInUser()
        setState(ProfileUiState.UpdateUserData(user))
    }

    override fun onCoroutineError(e: Exception) {

    }

}