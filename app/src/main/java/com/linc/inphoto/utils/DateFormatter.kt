package com.linc.inphoto.utils

import android.content.Context
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.pattern.DATE_PATTERN_DMY_DOT
import com.linc.inphoto.utils.extensions.pattern.DATE_PATTERN_SHORT_MD
import com.linc.inphoto.utils.extensions.pattern.DATE_TIME_PATTERN_SEMICOLON
import com.linc.inphoto.utils.extensions.pattern.TIME_PATTERN_SEMICOLON
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

    fun getDuration(
        context: Context,
        millis: Long,
    ): String {
        val stringFormat: Int
        val timeUnitCount: Long
        when {
            millis >= YEAR_IN_MILLIS -> {
                stringFormat = R.plurals.date_period_long_year
                timeUnitCount = millis.millisToYears()
            }
            millis >= MONTH_IN_MILLIS -> {
                stringFormat = R.plurals.date_period_long_month
                timeUnitCount = millis.millisToMonths()
            }
            millis >= WEEK_IN_MILLIS -> {
                stringFormat = R.plurals.date_period_long_week
                timeUnitCount = millis.millisToWeeks()
            }
            millis >= DAY_IN_MILLIS -> {
                stringFormat = R.plurals.date_period_long_day
                timeUnitCount = millis.millisToDays()
            }
            millis >= ONE_HOUR_IN_MILLIS -> {
                stringFormat = R.plurals.date_period_long_hour
                timeUnitCount = millis.millisToHours()
            }
            millis >= ONE_MINUTE_IN_MILLIS -> {
                stringFormat = R.plurals.date_period_long_minute
                timeUnitCount = millis.millisToMinutes()
            }
            else -> {
                stringFormat = R.plurals.date_period_long_second
                timeUnitCount = millis.millisToSeconds()
            }
        }
        return context.resources.getQuantityString(
            stringFormat,
            timeUnitCount.toInt(),
            timeUnitCount.toInt()
        )
    }

    fun format(millis: Long, pattern: String, locale: Locale = Locale.US) =
        SimpleDateFormat(pattern, locale).format(millis)

}