package com.linc.inphoto.ui.base.viewmodel

import com.github.terrakok.cicerone.Router
import com.linc.inphoto.ui.base.UiEffect
import com.linc.inphoto.ui.base.UiState

abstract class BaseStubViewModel(
    private val router: Router
) : BaseViewModel<UiState, UiEffect>(router)