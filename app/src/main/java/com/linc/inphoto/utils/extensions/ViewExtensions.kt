package com.linc.inphoto.utils.extensions

import android.view.View

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
    if(condition) enable() else disable()
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}