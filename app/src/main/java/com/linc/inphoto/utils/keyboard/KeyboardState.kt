package com.linc.inphoto.utils.keyboard

interface KeyboardState {
    val isKeyboardOpen: Boolean
    val keyBoardHeight: Int
    val screenHeight: Int

    fun observeState(listener: (Boolean) -> Unit)
}