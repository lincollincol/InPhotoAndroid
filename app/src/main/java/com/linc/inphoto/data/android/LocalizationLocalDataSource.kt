package com.linc.inphoto.data.android

import java.util.*
import javax.inject.Inject

class LocalizationLocalDataSource @Inject constructor() {

    suspend fun loadAvailableLanguages() = listOf(
        Locale.US,
        Locale("uk", "UA")
    )

}