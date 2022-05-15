package com.linc.inphoto.utils.extensions

import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding

fun ViewBinding.getString(@StringRes id: Int, vararg formatArgs: Any) =
    root.context.getString(id, formatArgs)
