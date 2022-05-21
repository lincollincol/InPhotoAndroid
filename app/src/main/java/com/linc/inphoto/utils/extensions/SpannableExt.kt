package com.linc.inphoto.utils.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorInt
import com.linc.inphoto.utils.view.span.SimpleClickableSpan

fun Spannable.addSpan(
    span: Any,
    start: Int = 0,
    end: Int = length,
    flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
): Spannable {
    setSpan(span, start, end, flags)
    return this
}

fun Spannable.bold(): Spannable = addSpan(
    StyleSpan(Typeface.BOLD)
)

fun Spannable.italic(): Spannable = addSpan(
    StyleSpan(Typeface.ITALIC)
)

fun Spannable.boldItalic(): Spannable = addSpan(
    StyleSpan(Typeface.BOLD_ITALIC)
)

fun Spannable.color(@ColorInt color: Int) = addSpan(
    ForegroundColorSpan(color)
)

fun Spannable.clickable(
    underlined: Boolean = false,
    action: () -> Unit
): Spannable = addSpan(SimpleClickableSpan(underlined, action))
