package com.linc.inphoto.ui.infodialog

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InfoMessageViewModel @Inject constructor(
    router: Router
) : BaseViewModel<EmptyUiState>(router) {

    override val _uiState = MutableStateFlow(EmptyUiState())

    fun finishInfo(resultKey: String?) {
        router.exit()
        resultKey?.let { router.sendResult(resultKey, Unit) }
    }

}