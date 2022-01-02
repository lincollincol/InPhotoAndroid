package com.linc.inphoto.ui.base.viewmodel

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.EmptyUiState
import com.linc.inphoto.ui.base.UiEffect
import com.linc.inphoto.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseStubViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel<UiState, UiEffect>(router) {
    override fun onCoroutineError(e: Exception) {
        // DO nothing
    }
}