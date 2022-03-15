package com.linc.inphoto.utils.extensions.view

import android.widget.EditText

fun EditText.textToString() = text.toString()

fun EditText.update(data: String?) {
    data?.let { text.replace(0, data.length, data) }
}

fun EditText.update(data: CharSequence?) {
    data?.let { update(it.toString()) }
}