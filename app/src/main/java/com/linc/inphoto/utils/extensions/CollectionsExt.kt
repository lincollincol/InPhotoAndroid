package com.linc.inphoto.utils.extensions

import com.linc.inphoto.utils.collections.ImmutableDeque
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


suspend fun <T, R> Iterable<T>.mapAsync(
    transform: suspend (T) -> R
): List<Deferred<R>> = coroutineScope {
    return@coroutineScope map { async { transform(it) } }
}

fun <T> ArrayDeque<T>?.toImmutableDeque() = ImmutableDeque(this ?: ArrayDeque())

fun <T> List<T>?.toImmutableDeque() = ImmutableDeque(ArrayDeque(this.orEmpty()))

fun <T> List<T>?.toMutableDeque() = ArrayDeque(this.orEmpty())

fun <T> Collection<T>.mapIf(
    condition: (T) -> Boolean,
    transform: (T) -> T
) = map { item -> if (condition(item)) transform(item) else item }

fun <T> MutableCollection<T>.update(items: Collection<T>) {
    clear()
    addAll(items)
}

fun <T> MutableCollection<T>.replace(old: T, new: T) =
    mapIf(condition = { it == old }, transform = { new })

fun <T> Collection<T>.indexOf(item: T, defaultIndex: Int) =
    indexOf(item).let { index -> if (index != -1) index else defaultIndex }

fun <T> Collection<T>.isValidIndex(index: Int) = index in 0 until size

inline fun <T> List(
    size: Int,
    startIndex: Int,
    init: (index: Int) -> T
): List<T> = List(size) { init(it + startIndex) }

