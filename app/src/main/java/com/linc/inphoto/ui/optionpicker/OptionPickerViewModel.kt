package com.linc.inphoto.ui.optionpicker

import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OptionPickerViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<EmptyUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(EmptyUiState())

    fun selectOption(resultKey: String?, option: OptionModel?) {
        router.closeDialog()
        if (resultKey != null && option != null) {
            router.sendResult(resultKey, option)
        }
    }

}