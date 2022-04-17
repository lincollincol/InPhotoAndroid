package com.linc.inphoto.ui.infodialog

import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InfoMessageViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<EmptyUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(EmptyUiState())

    fun finishInfo(resultKey: String?) {
        router.closeDialog()
        resultKey?.let { router.sendResult(resultKey, Unit) }
    }

}