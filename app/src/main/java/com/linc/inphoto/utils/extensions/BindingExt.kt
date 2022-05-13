package com.linc.inphoto.utils.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.viewbinding.ViewBinding

fun ViewBinding.getString(@StringRes id: Int, vararg formatArgs: Any) =
    root.context.getString(id, formatArgs)

fun ViewBinding.autoAnimateTargets(scene: ViewGroup, vararg targets: View) {
    TransitionManager.beginDelayedTransition(
        scene,
        AutoTransition().apply {
            targets.forEach(::addTarget)
        }
    )
}
