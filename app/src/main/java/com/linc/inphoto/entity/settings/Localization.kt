package com.linc.inphoto.entity.settings

import java.util.*

data class Localization(
    val locale: Locale,
    val isCurrentLocale: Boolean
)