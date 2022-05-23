package com.linc.inphoto.utils.extensions

import android.content.res.Resources
import android.util.TypedValue
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

val FIVE_SECONDS_IN_MILLIS get() = 5000L
val TEN_SECONDS_IN_MILLIS get() = FIVE_SECONDS_IN_MILLIS * 2
val FIFTEEN_SECONDS_IN_MILLIS get() = FIVE_SECONDS_IN_MILLIS * 3
val THIRTY_SECONDS_IN_MILLIS get() = FIFTEEN_SECONDS_IN_MILLIS * 2
val ONE_MINUTE_IN_MILLIS get() = THIRTY_SECONDS_IN_MILLIS * 2
val FIVE_MINUTES_IN_MILLIS get() = ONE_MINUTE_IN_MILLIS * 5
val TEN_MINUTES_IN_MILLIS get() = FIVE_MINUTES_IN_MILLIS * 2
val FIFTEEN_MINUTES_IN_MILLIS get() = FIVE_MINUTES_IN_MILLIS + TEN_MINUTES_IN_MILLIS
val THIRTY_MINUTES_IN_MILLIS get() = FIFTEEN_MINUTES_IN_MILLIS * 2
val ONE_HOUR_IN_MILLIS get() = THIRTY_MINUTES_IN_MILLIS * 2
val THREE_HOUR_IN_MILLIS get() = ONE_HOUR_IN_MILLIS * 3
val SIX_HOUR_IN_MILLIS get() = THREE_HOUR_IN_MILLIS * 2
val TWELVE_HOUR_IN_MILLIS get() = SIX_HOUR_IN_MILLIS * 2
val DAY_IN_MILLIS get() = TWELVE_HOUR_IN_MILLIS * 2
val WEEK_IN_MILLIS get() = DAY_IN_MILLIS * 7
val MONTH_IN_MILLIS get() = WEEK_IN_MILLIS * 4
val YEAR_IN_MILLIS get() = MONTH_IN_MILLIS * 12

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

fun Long.millisToSeconds() = this / 1000L

fun Long.millisToMinutes() = this / ONE_MINUTE_IN_MILLIS

fun Long.millisToHours() = this / ONE_HOUR_IN_MILLIS

fun Long.millisToDays() = this / DAY_IN_MILLIS

fun Long.millisToWeeks() = this / WEEK_IN_MILLIS

fun Long.millisToMonths() = this / MONTH_IN_MILLIS

fun Long.millisToYears() = this / YEAR_IN_MILLIS

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