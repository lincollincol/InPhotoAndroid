package com.linc.inphoto.utils.extensions

import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Transition
import com.linc.inphoto.utils.extensions.view.animateTargets
import com.linc.inphoto.utils.extensions.view.autoAnimateTargets
import com.linc.inphoto.utils.keyboard.FragmentKeyboardProperty

fun Fragment.showKeyboard(view: EditText) {
    view.requestFocus()
    requireContext().getKeyboard()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard() =
    requireContext().getKeyboard()?.hideSoftInputFromWindow(view?.applicationWindowToken, 0)

fun <T> Fragment.getArgument(key: String): T? = requireArguments().get(key) as? T

fun <T> Fragment.getArgument(key: String, default: T): T =
    requireArguments().get(key) as? T ?: default

fun Fragment.animateTargets(transition: Transition, scene: ViewGroup, vararg targets: View) =
    scene.animateTargets(transition, *targets)

fun Fragment.animateTargets(transition: Transition, scene: ViewGroup, targets: Collection<View>) =
    animateTargets(transition, scene, *targets.toTypedArray())

fun Fragment.animateTargets(transition: Transition, scene: ViewGroup, targets: Sequence<View>) =
    animateTargets(transition, scene, targets.toList())

fun Fragment.autoAnimateTargets(scene: ViewGroup, vararg targets: View) =
    scene.autoAnimateTargets(*targets)

fun Fragment.permissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(
        requireContext(), permission
    ) == PackageManager.PERMISSION_GRANTED

fun Fragment.permissionDisabled(permission: String) =
    !permissionGranted(permission) && !shouldShowRequestPermissionRationale(permission)

fun FragmentManager.findVisibleFragment() = fragments.firstOrNull { it.isVisible }

fun Fragment.keyboard() = FragmentKeyboardProperty()

fun Fragment.onSystemBackPressed() = requireActivity().onBackPressed()