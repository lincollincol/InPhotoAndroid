package com.linc.inphoto.utils.extensions

import kotlinx.coroutines.flow.MutableStateFlow

@Deprecated(
    "Duplicate of flow api",
    replaceWith = ReplaceWith(
        "Use standard update() function form flow api",
        "kotlin.coroutines.flow.update"
    )
)
fun <T> MutableStateFlow<T>.update(newState: T.() -> T) {
    value = newState(value)
}