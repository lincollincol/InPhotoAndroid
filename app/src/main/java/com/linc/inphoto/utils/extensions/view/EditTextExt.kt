package com.linc.inphoto.utils.extensions.view

import android.text.Editable
import android.widget.EditText

fun EditText.textToString() = text.toString()

fun Editable?.update(data: CharSequence?) {
    if (this == null || data == null) return
    clear()
    insert(0, data)
}

fun EditText.update(data: CharSequence?) {
    if (data == null) return
    setText(data)
    setSelection(data.length)
}

fun Editable?.deleteLast() {
    if (this == null || length == 0) return
    delete(length - 1, length)
}