package com.linc.inphoto.ui.datepicker

import com.linc.inphoto.ui.base.state.UiState

data class DurationPickerUiState(
    val valuesMillis: List<Long> = listOf(),
    val selectedValue: Int = 0
) : UiState