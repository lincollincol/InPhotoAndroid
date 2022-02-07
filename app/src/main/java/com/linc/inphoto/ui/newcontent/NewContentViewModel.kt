package com.linc.inphoto.ui.newcontent

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NewContentViewModel @Inject constructor(
    router: Router
) : BaseViewModel<NewContentUiState>(router) {
    override val _uiState = MutableStateFlow(NewContentUiState())

}