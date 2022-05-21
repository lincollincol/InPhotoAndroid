package com.linc.inphoto.utils.extensions

import com.linc.inphoto.utils.collections.ImmutableDeque


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