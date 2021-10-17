package com.linc.inphoto.ui.base

sealed class BaseEffect : UiEffect {
    object Loading : BaseEffect()
    object Error : BaseEffect()
}