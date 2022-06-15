package com.linc.inphoto.utils.extensions

import android.app.Activity

fun Activity.postDelayed(delay: Long, action: () -> Unit) {
    window.decorView.postDelayed(Runnable { action() }, delay)
}