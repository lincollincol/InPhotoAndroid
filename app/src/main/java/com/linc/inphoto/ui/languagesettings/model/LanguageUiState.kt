package com.linc.inphoto.ui.languagesettings.model

import com.linc.inphoto.entity.settings.Localization
import com.linc.inphoto.ui.base.state.ItemUiState
import java.util.*

data class LanguageUiState(
    val locale: Locale,
    val isCurrentLocale: Boolean,
    val onClick: () -> Unit,
) : ItemUiState {
    override fun getStateItemId(): Long = locale.language.hashCode().toLong()
}

fun Localization.toUiState(onClick: () -> Unit) = LanguageUiState(
    locale,
    isCurrentLocale,
    onClick = onClick
)