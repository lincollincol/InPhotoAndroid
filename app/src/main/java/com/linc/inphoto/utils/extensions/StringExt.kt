package com.linc.inphoto.utils.extensions

val String.Companion.EMPTY get() = ""

fun String?.toException() = Exception(this)

fun String.removeSpaces() = filter { !it.isWhitespace() }

fun Char.isOneOf(vararg symbols: Char) = symbols.any { this == it }