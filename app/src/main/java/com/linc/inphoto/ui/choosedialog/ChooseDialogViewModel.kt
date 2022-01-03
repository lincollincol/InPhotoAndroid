package com.linc.inphoto.ui.choosedialog

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.ScreenResultKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ChooseDialogViewModel @Inject constructor(
    private val router: Router
) : BaseViewModel<ChooseUiState>(router) {

    fun onFinishWithResult(position: Int) {
        router.sendResult(ScreenResultKey.CHOOSE_OPTION_RESULT, position)
        onBackPressed()
    }


    override val _uiState = MutableStateFlow<ChooseUiState>(ChooseUiState())

}