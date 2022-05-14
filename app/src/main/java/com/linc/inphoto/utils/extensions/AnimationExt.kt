package com.linc.inphoto.utils.extensions

import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition
import com.linc.inphoto.utils.extensions.view.animateTargets
import com.linc.inphoto.utils.extensions.view.autoAnimateTargets

fun animateTargets(transition: Transition, scene: ViewGroup, vararg targets: View) =
    scene.animateTargets(transition, *targets)

fun animateTargets(transition: Transition, scene: ViewGroup, targets: Collection<View>) =
    scene.animateTargets(transition, scene, targets)

fun animateTargets(transition: Transition, scene: ViewGroup, targets: Sequence<View>) =
    scene.animateTargets(transition, scene, targets)

fun autoAnimateTargets(scene: ViewGroup, vararg targets: View) =
    scene.autoAnimateTargets(*targets)
