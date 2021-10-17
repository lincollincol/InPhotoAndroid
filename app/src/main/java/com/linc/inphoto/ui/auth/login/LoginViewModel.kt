package com.linc.inphoto.ui.auth.login

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel<LoginUiState, LoginUiEffect>() {

    override fun onCoroutineError(e: Exception) {

    }


}