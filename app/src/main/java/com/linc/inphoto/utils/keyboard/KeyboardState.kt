package com.linc.inphoto.utils.keyboard

interface KeyboardState {
    fun observeState(listener: (Boolean) -> Unit)
}