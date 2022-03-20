package com.linc.inphoto.utils.extensions.view

import android.animation.ObjectAnimator
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView

fun ScrollView.scrollToBottom(
    container: ViewGroup,
    duration: Long = 1000,
) = ObjectAnimator.ofInt(this, "scrollY", container.height)
    .setDuration(duration)
    .start()

fun NestedScrollView.scrollToBottom(
    container: ViewGroup,
    duration: Long = 1000,
) = ObjectAnimator.ofInt(this, "scrollY", container.height)
    .setDuration(duration)
    .start()