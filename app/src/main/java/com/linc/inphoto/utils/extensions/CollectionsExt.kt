package com.linc.inphoto.utils.extensions

import com.linc.inphoto.utils.collections.ImmutableDeque


fun <T> ArrayDeque<T>?.toImmutableDeque() = ImmutableDeque(this ?: ArrayDeque())

fun <T> List<T>?.toImmutableDeque() = ImmutableDeque(ArrayDeque(this.orEmpty()))

fun <T> Collection<T>.mapIf(
    predicate: (T) -> Boolean,
    transform: (T) -> T
) = map { item -> if (predicate(item)) transform(item) else item }