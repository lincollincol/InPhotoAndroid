package com.linc.inphoto.utils.extensions

inline fun <T : Any> anyNull(vararg any: T?, onNull: () -> Nothing): List<T> =
    any.filterNotNull().takeIf { any.size == it.size } ?: onNull()

inline fun <T : Any, R> anyLet(vararg any: T?, let: (List<T>) -> R): R? =
    any.filterNotNull().takeIf { any.size == it.size }?.let(let)

fun <T : Any> hasNull(vararg any: T?): Boolean = any.filterNotNull().size != any.size

fun <T : Any> hasNotNull(vararg any: T?): Boolean = any.filterNotNull().isNotEmpty()

inline fun <T : Any, R> allNull(vararg any: T?, let: (List<T>) -> R): R? =
    any.filterNotNull().takeIf { it.isEmpty() }?.let(let)

fun <A, B> Pair<A?, B?>.filterNull(): Pair<A, B>? =
    first?.let { firstNonNull ->
        second?.let { secondNonNull ->
            Pair(firstNonNull, secondNonNull)
        }
    }

fun <T> T.oneOf(vararg options: T): Boolean = options.any { it == this }
