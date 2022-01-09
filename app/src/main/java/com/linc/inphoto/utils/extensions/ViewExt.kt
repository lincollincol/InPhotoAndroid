package com.linc.inphoto.utils.extensions

import android.view.View
import android.view.ViewGroup

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