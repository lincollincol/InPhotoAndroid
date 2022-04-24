package com.linc.inphoto.utils

import android.content.Context
import com.linc.inphoto.R
import com.linc.inphoto.utils.extensions.EMPTY
import com.linc.inphoto.utils.extensions.weeks
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

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
            else -> String.EMPTY
        }
    }

}