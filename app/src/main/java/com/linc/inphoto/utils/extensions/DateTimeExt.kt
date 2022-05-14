package com.linc.inphoto.utils.extensions

import kotlinx.datetime.*

val DateTimePeriod.weeks: Int
    get() {
        // 1 year = 52 weeks
        val yWeeks = years * 52
        // 1 month = 4 weeks
        val mWeeks = months * 4
        val dWeeks = days / 7
        return yWeeks + mWeeks + dWeeks
    }

fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()) =
    Clock.System.now().toLocalDateTime(timeZone)

fun LocalDateTime.Companion.from(
    millis: Long,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = Instant.fromEpochMilliseconds(millis).toLocalDateTime(timeZone)

fun LocalDateTime.isToday(date: LocalDateTime): Boolean {
    return year == date.year && dayOfYear == date.dayOfYear
}

fun LocalDateTime.isToday(): Boolean {
    val todayDateTime = LocalDateTime.now()
    return year == todayDateTime.year && dayOfYear == todayDateTime.dayOfYear
}