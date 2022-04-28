package com.linc.inphoto.utils.keyboard

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import kotlin.math.abs

class KeyboardStateHelper(
    val withStatusBar: Boolean = false,
    var onKeyboardOpen: ((Boolean) -> Unit)? = null,
    var onHeightChanged: ((Int) -> Unit)? = null
) : ViewTreeObserver.OnGlobalLayoutListener, KeyboardState {

    override var isKeyboardOpen = false
        private set(value) {
            field = value
            keyboardVisibilityListener?.invoke(value)
        }

    override var keyBoardHeight = 0
        private set

    override var screenHeight = 0
        private set

    private var rootView: View? = null
    private var previousHeight = -1
    private var keyboardVisibilityListener: ((Boolean) -> Unit)? = null

    override fun onGlobalLayout() {
        val height = rootView.screenVisibleHeight()
        var modified = false
        if (previousHeight != -1 && previousHeight != height) {
            if (previousHeight > height && !isKeyboardOpen) {
                isKeyboardOpen = isKeyboardOpen(height)
                modified = true
            } else if (previousHeight < height && isKeyboardOpen) {
                isKeyboardOpen = isKeyboardOpen(height)
                modified = true
            }
        }
        if (previousHeight != height) {
            keyBoardHeight = abs(previousHeight - height)
            previousHeight = height
            onHeightChanged?.invoke(height)
        }
        if (modified) {
            onKeyboardOpen?.invoke(isKeyboardOpen(height))
        }
    }

    fun attach(rootView: View?) {
        this.rootView = rootView
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        screenHeight = rootView.screenVisibleHeight()
    }

    fun detach() {
        rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        this.rootView = null
        this.keyboardVisibilityListener = null
    }

    override fun observeState(listener: (Boolean) -> Unit) {
        this.keyboardVisibilityListener = listener
    }

    private fun isKeyboardOpen(height: Int) = screenHeight > height

    private fun View?.screenVisibleHeight(): Int {
        val rect = Rect()
        this?.getWindowVisibleDisplayFrame(rect)
        return rect.bottom - (rect.top.takeIf { withStatusBar } ?: 0)
    }

}