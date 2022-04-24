package com.linc.inphoto.utils.extensions

import kotlinx.datetime.DateTimePeriod

val DateTimePeriod.weeks: Int
    get() {
        // 1 year = 52 weeks
        val yWeeks = years * 52
        // 1 month = 4 weeks
        val mWeeks = months * 4
        val dWeeks = days / 7
        return yWeeks + mWeeks + dWeeks
    }