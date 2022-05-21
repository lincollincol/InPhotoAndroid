package com.linc.inphoto.utils.extensions

import android.content.res.Resources
import android.util.TypedValue
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

val Number.toPx get() = applyNumberDimension(this, TypedValue.COMPLEX_UNIT_DIP)

val Number.toSp get() = applyNumberDimension(this, TypedValue.COMPLEX_UNIT_SP)

private fun applyNumberDimension(
    number: Number,
    unit: Int
) = TypedValue.applyDimension(
    unit,
    number.toFloat(),
    Resources.getSystem().displayMetrics
)

fun Long.compactNumber(): String {
    if (this < 1000) return this.toString()
    val exp = (ln(toDouble()) / ln(1000.0)).toInt()
    return String.format(
        Locale.US,
        "%.1f%c",
        this / 1000.0.pow(exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
}

fun Int.compactNumber(): String = toLong().compactNumber()