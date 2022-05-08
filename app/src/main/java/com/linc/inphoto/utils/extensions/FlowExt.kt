package com.linc.inphoto.utils.extensions

import kotlinx.coroutines.flow.*

suspend fun <T : Any> Flow<T?>.collectNotNull(
    action: suspend (value: T) -> Unit
) = filterNotNull().collect(action)

suspend fun <T> MutableSharedFlow<T>.update(function: (state: T) -> T) {
    val value = firstOrNull() ?: return
    emit(function(value))
}

suspend fun <T : Any> Flow<T>.collect(
    ignoreInitialValue: Boolean = false,
    action: suspend (value: T) -> Unit
) {
    collectIndexed { index, value ->
        when {
            ignoreInitialValue && index == 0 -> drop(1)
            else -> this
        }.collect(action)
    }
//    when {
//        ignoreInitialValue -> drop(1)
//        else -> this
//    }.collect(action)
}
