package com.linc.inphoto.ui.optionpicker

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OptionPickerViewModel @Inject constructor(
    router: Router
) : BaseViewModel<EmptyUiState>(router) {

    fun onFinishWithResult(option: OptionModel?) {
        onBackPressed()
        if (option != null) {
            router.sendResult(Navigation.NavResult.CHOOSE_OPTION_RESULT, option)
        }
    }

    override val _uiState = MutableStateFlow(EmptyUiState())

}