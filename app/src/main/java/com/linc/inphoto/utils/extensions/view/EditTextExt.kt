package com.linc.inphoto.utils.extensions.view

import android.text.Editable
import android.widget.EditText

fun EditText.textToString() = text.toString()

fun Editable?.update(data: CharSequence?) {
    this ?: return
    data?.let { replace(0, data.length, data) }
}

fun Editable?.deleteLast() {
    this ?: return
    delete(length - 1, length)
}