package com.linc.inphoto.utils

import android.content.Context
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.pattern.DATE_PATTERN_DMY_DOT
import com.linc.inphoto.utils.extensions.pattern.DATE_PATTERN_SHORT_MD
import com.linc.inphoto.utils.extensions.pattern.DATE_TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.weeks
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    fun getRelativeTimeSpanString(context: Context, millis: Long): String {
        val period = Instant.fromEpochMilliseconds(millis)
            .periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())

        return when {
            period.weeks > 0 -> context.resources.getQuantityString(
                R.plurals.date_period_short_week,
                period.weeks,
                period.weeks
            )
            period.days > 0 -> context.getString(R.string.date_period_short_day, period.days)
            period.hours > 0 -> context.getString(R.string.date_period_short_hour, period.hours)
            period.minutes > 0 -> context.getString(
                R.string.date_period_short_minute,
                period.minutes
            )
            period.seconds > 0 -> context.getString(
                R.string.date_period_short_second,
                period.seconds
            )
            else -> context.getString(R.string.date_period_short_now)
        }
    }

    fun getRelativeTimeSpanString2(
        millis: Long,
        locale: Locale
    ): String {
        val period = Instant.fromEpochMilliseconds(millis)
            .periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())

        val pattern = when {
            period.years > 0 -> DATE_PATTERN_DMY_DOT
            period.weeks > 0 -> DATE_PATTERN_SHORT_MD
            period.days > 0 -> DATE_TIME_PATTERN_SEMICOLON
            else -> TIME_PATTERN_SEMICOLON
        }

        return SimpleDateFormat(pattern, locale).format(millis)
    }

    fun format(millis: Long, pattern: String, locale: Locale) =
        SimpleDateFormat(pattern, locale).format(millis)

}