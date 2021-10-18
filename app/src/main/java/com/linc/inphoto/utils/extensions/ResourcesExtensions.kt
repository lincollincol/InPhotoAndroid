package com.linc.inphoto.utils.extensions

import android.app.Activity
import android.content.Context
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

fun <T : Number> Activity.getDimension(@DimenRes id: Int) = resources.getDimension(id) as T

fun <T : Number> Fragment.getDimension(@DimenRes id: Int) = resources.getDimension(id) as T