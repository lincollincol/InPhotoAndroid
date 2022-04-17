package com.linc.inphoto.utils.extensions

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.transition.Transition
import com.linc.inphoto.utils.extensions.view.animateTargets
import com.linc.inphoto.utils.extensions.view.autoAnimateTargets

fun Fragment.showKeyboard(view: EditText) {
    view.requestFocus()
    requireContext().getKeyboard()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard() =
    requireContext().getKeyboard()?.hideSoftInputFromWindow(view?.applicationWindowToken, 0)

fun <T> Fragment.getArgument(key: String): T? = requireArguments().get(key) as? T

fun <T> Fragment.getArgument(key: String, default: T): T =
    requireArguments().get(key) as? T ?: default

fun Fragment.animateTargets(
    transition: Transition,
    scene: ViewGroup,
    vararg targets: View
) = scene.animateTargets(transition, *targets)

fun Fragment.autoAnimateTargets(scene: ViewGroup, vararg targets: View) =
    scene.autoAnimateTargets(*targets)