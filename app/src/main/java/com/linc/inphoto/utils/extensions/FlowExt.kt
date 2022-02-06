package com.linc.inphoto.utils.extensions

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.update(newState: T.() -> T) {
    value = newState(value)
}