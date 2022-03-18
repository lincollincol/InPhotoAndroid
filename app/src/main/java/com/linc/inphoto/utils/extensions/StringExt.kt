package com.linc.inphoto.utils.extensions

fun String?.toException() = Exception(this)

val String.Companion.EMPTY get() = ""