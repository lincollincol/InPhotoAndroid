package com.linc.inphoto.utils.extensions

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard(view: EditText) {
    view.requestFocus()
    requireContext().getKeyboard()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard() =
    requireContext().getKeyboard()?.hideSoftInputFromWindow(view?.applicationWindowToken, 0)
