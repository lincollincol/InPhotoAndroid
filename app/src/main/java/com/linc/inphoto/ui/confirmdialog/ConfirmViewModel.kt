package com.linc.inphoto.ui.confirmdialog

import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<EmptyUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(EmptyUiState())
    private var resultKey: String? = null

    fun applyResultKey(resultKey: String?) {
        this.resultKey = resultKey
    }

    fun confirm() {
        finishWithResult(true)
    }

    fun cancel() {
        finishWithResult(false)
    }

    private fun finishWithResult(result: Boolean) {
        resultKey ?: return
        router.run {
            closeDialog()
            sendResult(resultKey.orEmpty(), result)
        }
    }
}