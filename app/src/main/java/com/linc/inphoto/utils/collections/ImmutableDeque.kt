package com.linc.inphoto.utils.collections

class ImmutableDeque<T>(private val deque: ArrayDeque<T>) : List<T> by deque {
    fun toMutableDeque() = deque
}