package com.linc.inphoto.utils.view

import android.view.Gravity
import androidx.core.widget.NestedScrollView

class VerticalNestedScrollListener(
    private val onScrolled: (Int) -> Unit
) : NestedScrollView.OnScrollChangeListener {
    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val gravity = when {
            scrollY > oldScrollY -> Gravity.BOTTOM
            scrollY < oldScrollY -> Gravity.TOP
            else -> return
        }
        onScrolled(gravity)
    }
}