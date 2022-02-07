package com.linc.inphoto.ui.choosedialog

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.state.EmptyUiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.ui.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChooseDialogViewModel @Inject constructor(
    router: Router
) : BaseViewModel<EmptyUiState>(router) {

    fun onFinishWithResult(option: ChooseOptionModel?) {
        if (option != null) {
            router.sendResult(Navigation.NavResult.CHOOSE_OPTION_RESULT, option)
        }
        onBackPressed()
    }

    override val _uiState = MutableStateFlow(EmptyUiState())

}