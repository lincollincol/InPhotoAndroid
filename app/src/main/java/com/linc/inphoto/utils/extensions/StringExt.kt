package com.linc.inphoto.utils.extensions

import android.text.Spannable
import android.text.SpannableString
import com.linc.inphoto.utils.span.SpanReplaceBuilder

val String.Companion.EMPTY get() = ""

fun String?.toException() = Exception(this)

fun CharSequence?.isEmptyOrBlank() = isNullOrEmpty() || isBlank()

fun CharSequence?.isLongerThan(len: Int) = this?.length ?: 0 > len

fun CharSequence.isOneOf(vararg targets: CharSequence) = targets.any { this == it }

fun CharSequence.containsOneOf(vararg targets: CharSequence) = targets.any { contains(it) }

fun Char?.isOneOf(vararg symbols: Char) = symbols.any { this == it }

fun StringBuilder.update(text: String) = replace(0, length, text)

fun StringBuffer.update(text: String) = replace(0, length, text)

fun CharSequence?.replaceSpans(builderFunc: SpanReplaceBuilder.() -> Unit) =
    SpanReplaceBuilder(this.toString()).apply(builderFunc).finalize()

fun CharSequence.addBoldSpan(target: String): CharSequence? = addBoldSpans(listOf(target))

fun CharSequence.addClickableSpan(target: String, clickListener: () -> Unit): CharSequence? =
    addClickableSpans(listOf(target), clickListener)

fun CharSequence.addBoldSpans(targets: List<String>): CharSequence? =
    addModifiedSpans(targets) { it.bold() }

fun CharSequence.addClickableSpans(
    targets: List<String>,
    clickListener: () -> Unit
): CharSequence? = addModifiedSpans(targets) { it.clickable(action = clickListener) }

fun CharSequence.addModifiedSpans(
    targets: List<String>,
    modifier: (Spannable) -> Spannable
): CharSequence? = replaceSpans {
    addSpan(targets, { it }) { modifier(toSpannable()) }
}.takeIf { it.isNotEmpty() }

fun CharSequence.addModifiedSpan(
    target: String,
    modifier: (Spannable) -> Spannable
): CharSequence? = addModifiedSpans(listOf(target), modifier)

fun CharSequence.toSpannable() = when (this) {
    is Spannable -> this
    else -> SpannableString(this)
}

fun CharSequence.extractWord(index: Int, vararg delimiters: Char): String {
    var startIndex = index
    var endIndex = index
    while (!getOrNull(startIndex).isOneOf(*delimiters) && startIndex > 0) startIndex--
    while (!getOrNull(endIndex).isOneOf(*delimiters) && endIndex < length) endIndex++
    return substring(startIndex, endIndex).trim()
}