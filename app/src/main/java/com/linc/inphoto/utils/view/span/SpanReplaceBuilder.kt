package com.linc.inphoto.utils.view.span

import android.text.SpannableString
import android.text.SpannableStringBuilder

class SpanReplaceBuilder(private val source: String) {

    private val replacers = mutableMapOf<String, (String) -> CharSequence>()

    fun addSpan(vararg targets: String, replacer: String.() -> CharSequence): SpanReplaceBuilder {
        targets.forEach { replacers[it] = replacer }
        return this
    }

    fun addSpan(
        targets: Collection<String>,
        replacer: String.() -> CharSequence
    ): SpanReplaceBuilder {
        targets.forEach { replacers[it] = replacer }
        return this
    }

    inline fun <T> addSpan(
        targets: Collection<T>,
        extractor: (T) -> String,
        crossinline replacer: String.(T) -> CharSequence
    ) {
        targets.forEach { data ->
            addSpan(extractor(data)) {
                replacer(data)
            }
        }
    }

    fun finalize(): CharSequence {
        val targets = replacers.keys

        val builder = SpannableStringBuilder()
        var position = 0

        while (position < source.length) {
            val targetInstance = source.findAnyOf(targets, startIndex = position)
            builder.append(source, position, targetInstance?.first ?: source.length)
            position = targetInstance?.let { it.first + it.second.length } ?: source.length

            targetInstance?.let { (_, target) ->
                builder.append(replacers[target]?.invoke(target) ?: "")
            }
        }

        return SpannableString(builder)
    }
}