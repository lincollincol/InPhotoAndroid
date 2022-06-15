package com.linc.inphoto.data.android

import com.linc.inphoto.entity.settings.Localization
import java.util.*
import javax.inject.Inject

class LocalizationLocalDataSource @Inject constructor() {

    suspend fun loadAvailableLanguages(): List<Localization> {
        return listOf(
            Locale.US,
            Locale("uk", "UA")
        ).map {
            Localization(it, it.equals(Locale.getDefault()))
        }
    }

}