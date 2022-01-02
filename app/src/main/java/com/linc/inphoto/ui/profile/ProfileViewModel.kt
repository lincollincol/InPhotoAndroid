package com.linc.inphoto.ui.profile

import android.Manifest
import android.net.Uri
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.UsersRepository
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.ui.navigation.AppScreens
import com.linc.inphoto.ui.navigation.ScreenResultKey
import com.linc.inphoto.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val router: Router,
    private val usersRepository: UsersRepository
) : BaseViewModel<ProfileUiState, BaseUiEffect>(router) {

    fun loadProfileData() = launchCoroutine {
        val user = usersRepository.getLoggedInUser()
        setState(ProfileUiState.UpdateUserData(user))
    }

    fun onUpdateAvatar(options: List<ChooseOptionModel>) = launchCoroutine {
        if(options.all { !it.enabled }) {
            router.navigateTo(AppScreens.InfoMessageScreen(
                R.string.permissions,
                R.string.profile_permissions_message
            ))
            return@launchCoroutine
        }
        router.setResultListener(ScreenResultKey.CHOOSE_OPTION_RESULT) {
            val position = it as Int
            // TODO: 28.11.21 open file picker or camera 
//            println(photoUri.path)
        }
        router.navigateTo(AppScreens.ChooseOptionScreen(options))
    }

    override fun onCoroutineError(e: Exception) {

    }

}