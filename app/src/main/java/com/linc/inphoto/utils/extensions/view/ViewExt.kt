package com.linc.inphoto.utils.extensions.view

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.linc.inphoto.utils.extensions.safeCast

fun View.show(condition: Boolean) {
    if(condition) show() else hide()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.dissolve() {
    visibility = View.INVISIBLE
}

fun View.enable(condition: Boolean) {
    if (condition) enable() else disable()
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.select(condition: Boolean) {
    if (condition) select() else deselect()
}

fun View.select() {
    isSelected = true
}

fun View.deselect() {
    isSelected = false
}

fun View.setMargin(
    startMargin: Int = 0,
    topMargin: Int = 0,
    endMargin: Int = 0,
    bottomMargin: Int = 0
) {
    val layoutParams = layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
    layoutParams?.setMargins(
        startMargin,
        topMargin,
        endMargin,
        bottomMargin
    )
    this.layoutParams = layoutParams
}

fun View.changePadding(
    startPadding: Int = paddingStart,
    topPadding: Int = paddingTop,
    endPadding: Int = paddingEnd,
    bottomPadding: Int = paddingBottom
) {
    setPadding(
        startPadding,
        topPadding,
        endPadding,
        bottomPadding
    )
}

fun View.marginLeft() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.leftMargin
        ?: 0

fun View.marginTop() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.topMargin
        ?: 0

fun View.marginRight() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.rightMargin
        ?: 0

fun View.marginBottom() =
    layoutParams
        ?.safeCast<ViewGroup.MarginLayoutParams>()
        ?.bottomMargin
        ?: 0

fun View.onThrottledClick(
    throttleDelay: Long = 500L,
    onClick: (View) -> Unit
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun View.setBackgroundRipple(@ColorInt color: Int) {
    background = RippleDrawable(
        ColorStateList(arrayOf(intArrayOf()), intArrayOf(color)),
        background,
        null
    )
}