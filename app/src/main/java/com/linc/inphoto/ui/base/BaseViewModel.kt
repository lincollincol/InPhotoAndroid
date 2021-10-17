package com.linc.inphoto.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract fun onEvent(event: UiEvent)



}