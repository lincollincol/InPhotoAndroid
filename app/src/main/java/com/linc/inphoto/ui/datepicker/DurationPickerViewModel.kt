package com.linc.inphoto.ui.datepicker

import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.utils.extensions.indexOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DurationPickerViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<DurationPickerUiState>(navContainerHolder) {

    override val _uiState = MutableStateFlow(DurationPickerUiState())

    fun applyPickerConfig(
        selectedDuration: Long,
        durationMillisOptions: List<Long>
    ) {
        _uiState.update {
            it.copy(
                selectedValue = durationMillisOptions.indexOf(selectedDuration, 0),
                valuesMillis = durationMillisOptions
            )
        }
    }

    fun selectValue(position: Int) {
        _uiState.update { it.copy(selectedValue = position) }
    }

    fun cancelPicker() {
        router.closeDialog()
    }

    fun confirmValue(resultKey: String?) {
        val selectedValue = currentState.valuesMillis[currentState.selectedValue]
        router.closeDialog()
        if (resultKey != null) {
            router.sendResult(resultKey, selectedValue)
        }
    }

}