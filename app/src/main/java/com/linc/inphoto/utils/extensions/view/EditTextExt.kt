package com.linc.inphoto.utils.extensions.view

import android.text.Editable
import android.widget.EditText
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.linc.inphoto.utils.extensions.EMPTY

fun EditText.textToString() = text.toString()

fun Editable?.update(data: CharSequence?) {
    if (this == null || data == null) return
    clear()
    insert(0, data)
}

fun EditText.update(data: CharSequence?) {
    if (data.isNullOrEmpty()) return editableText.clear()
    when {
        editableText.isNullOrEmpty() -> {
            setText(data)
            cursorToEnd()
        }
        else -> editableText.replace(0, editableText.length, data)
    }
}

fun EditText.cursorToEnd() = setSelection(length())

fun Editable?.deleteLast() {
    if (this == null || length == 0) return
    delete(length - 1, length)
}

fun TextInputLayout.setError(
    enabled: Boolean,
    @StringRes message: Int
) {
    error = if (enabled) context.getString(message) else String.EMPTY
    isErrorEnabled = enabled
}