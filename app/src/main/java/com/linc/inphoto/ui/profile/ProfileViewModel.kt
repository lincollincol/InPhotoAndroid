package com.linc.inphoto.ui.profile

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel<ProfileUiState, BaseUiEffect>(router) {

    override fun onCoroutineError(e: Exception) {

    }

}