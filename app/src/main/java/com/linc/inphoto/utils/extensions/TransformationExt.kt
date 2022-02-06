package com.linc.inphoto.utils.extensions

inline fun <reified T> Any.cast(): T = this as T

inline fun <reified T> Any.safeCast(): T? = this as? T
