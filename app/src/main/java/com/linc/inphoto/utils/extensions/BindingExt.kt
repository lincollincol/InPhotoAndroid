package com.linc.inphoto.utils.extensions

import android.content.Context
import androidx.viewbinding.ViewBinding

val ViewBinding.context: Context get() = root.context