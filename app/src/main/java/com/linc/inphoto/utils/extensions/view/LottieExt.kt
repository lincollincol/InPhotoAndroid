package com.linc.inphoto.utils.extensions.view

import android.animation.Animator
import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.playOneTime(
    onAnimationEnd: (() -> Unit)? = null
) {
    show()
    progress = 0f
    playAnimation()
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator?) {}
        override fun onAnimationEnd(p0: Animator?) {
            removeAllAnimatorListeners()
            hide()
            onAnimationEnd?.invoke()
        }

        override fun onAnimationCancel(p0: Animator?) {}
        override fun onAnimationRepeat(p0: Animator?) {}
    })
}