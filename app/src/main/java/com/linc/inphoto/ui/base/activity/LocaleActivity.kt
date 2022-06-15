package com.linc.inphoto.ui.base.activity

import java.util.*

interface LocaleActivity {
    fun setLocale(locale: Locale)
    val currentLocale: Locale
}