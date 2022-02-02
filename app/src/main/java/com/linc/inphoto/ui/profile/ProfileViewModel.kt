package com.linc.inphoto.ui.profile

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.R
import com.linc.inphoto.data.repository.UsersRepository
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.ui.navigation.AppScreens
import com.linc.inphoto.ui.navigation.ScreenResultKey
import com.linc.inphoto.utils.extensions.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val router: Router,
    private val usersRepository: UsersRepository
) : BaseViewModel<ProfileUiState>(router) {

    override val _uiState = MutableStateFlow(ProfileUiState())

    fun loadProfileData() = viewModelScope.launch {
        try {
            val user = usersRepository.getLoggedInUser()
            _uiState.update { copy(user = user) }
        } catch (e: Exception) {

        }
    }

    fun onUpdateAvatar(options: List<ChooseOptionModel>) = viewModelScope.launch {
        try {
            if (options.all { !it.enabled }) {
                router.navigateTo(
                    AppScreens.Common.InfoMessageScreen(
                        R.string.permissions,
                        R.string.profile_permissions_message
                    )
                )
                return@launch
            }
            router.setResultListener(ScreenResultKey.CHOOSE_OPTION_RESULT) {
                val position = it as Int
                // TODO: 28.11.21 open file picker or camera
//            println(photoUri.path)
            }
            router.navigateTo(AppScreens.Common.ChooseOptionScreen(options))

        } catch (e: Exception) {

        }
    }

}