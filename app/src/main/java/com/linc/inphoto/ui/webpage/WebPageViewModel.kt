package com.linc.inphoto.ui.webpage

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WebPageViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<WebPageUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(WebPageUiState())

    fun applyPageParams(title: String?, url: String?) {
        _uiState.update { it.copy(title = title, pageUrl = url) }
    }

}