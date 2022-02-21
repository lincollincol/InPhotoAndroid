package com.linc.inphoto.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

fun Fragment.showKeyboard(view: EditText) {
    view.requestFocus()
    requireContext().getKeyboard()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard() =
    requireContext().getKeyboard()?.hideSoftInputFromWindow(view?.applicationWindowToken, 0)

fun <T> Fragment.getArgument(key: String): T? = requireArguments().get(key) as? T

fun Fragment.autoAnimateTargets(scene: ViewGroup, vararg targets: View) {
    TransitionManager.beginDelayedTransition(
        scene,
        AutoTransition().apply {
            targets.forEach(::addTarget)
        }
    )
}