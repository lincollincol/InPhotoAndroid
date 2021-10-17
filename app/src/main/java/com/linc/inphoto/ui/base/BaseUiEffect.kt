package com.linc.inphoto.ui.base

sealed class BaseUiEffect : UiEffect {
    object Loading : BaseUiEffect()
    object Error : BaseUiEffect()
}