package com.linc.inphoto.utils.extensions

val String.Companion.EMPTY get() = ""

fun String?.toException() = Exception(this)

fun CharSequence?.isEmptyOrBlank() = isNullOrEmpty() || isBlank()
fun CharSequence?.isLongerThan(len: Int) = this?.length ?: 0 > len

fun Char.isOneOf(vararg symbols: Char) = symbols.any { this == it }