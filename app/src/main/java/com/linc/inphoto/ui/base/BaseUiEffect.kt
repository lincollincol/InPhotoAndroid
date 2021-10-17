package com.linc.inphoto.ui.base

sealed class BaseUiEffect : UiEffect {
    object Loading : BaseUiEffect()
    data class Error(val message: String) : BaseUiEffect()
}