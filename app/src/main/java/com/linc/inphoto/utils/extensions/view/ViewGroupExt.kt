package com.linc.inphoto.utils.extensions.view

import android.view.View
import android.view.ViewGroup
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager

fun ViewGroup.animateTargets(transition: Transition, vararg targets: View) {
    TransitionManager.beginDelayedTransition(
        this,
        transition.apply {
            targets.forEach(::addTarget)
        }
    )
}

fun ViewGroup.autoAnimateTargets(vararg targets: View) =
    animateTargets(AutoTransition(), *targets)

fun ViewGroup.animateTargets(transition: Transition, scene: ViewGroup, vararg targets: View) =
    scene.animateTargets(transition, *targets)

fun ViewGroup.animateTargets(transition: Transition, scene: ViewGroup, targets: Collection<View>) =
    animateTargets(transition, scene, *targets.toTypedArray())

fun ViewGroup.animateTargets(transition: Transition, scene: ViewGroup, targets: Sequence<View>) =
    animateTargets(transition, scene, targets.toList())