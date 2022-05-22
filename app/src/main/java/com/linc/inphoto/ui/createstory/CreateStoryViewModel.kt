package com.linc.inphoto.ui.createstory

import android.net.Uri
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CreateStoryViewModel @Inject constructor(
    navContainerHolder: NavContainerHolder
) : BaseViewModel<CreateStoryUiState>(navContainerHolder) {

    companion object {
        private const val DATE_PICKER_RESULT = "date_result"
        private const val DURATION_MULTIPLIER_GAPS = 6
        private const val DURATION_GAP_START_INDEX = 1
    }

    override val _uiState = MutableStateFlow(CreateStoryUiState())

    fun applyStoryContent(contentUri: Uri?) {
        _uiState.update { it.copy(contentUri = contentUri) }
    }

    fun selectExpirationTime() {
        val durationOptions = listOf(
            FIFTEEN_MINUTES_IN_MILLIS,
            THIRTY_MINUTES_IN_MILLIS,
            ONE_HOUR_IN_MILLIS,
            THREE_HOUR_IN_MILLIS,
            SIX_HOUR_IN_MILLIS,
            TWELVE_HOUR_IN_MILLIS,
            DAY_IN_MILLIS,
            WEEK_IN_MILLIS
        )
        selectTimeMillis(currentState.expirationTimeMillis, durationOptions) { millis ->
            _uiState.update { it.copy(expirationTimeMillis = millis) }
        }
    }

    fun selectDurationTime() {
        val durationOptions = List(
            DURATION_MULTIPLIER_GAPS,
            DURATION_GAP_START_INDEX
        ) { TEN_SECONDS_IN_MILLIS * it }
        selectTimeMillis(currentState.durationMillis, durationOptions) { millis ->
            _uiState.update { it.copy(durationMillis = millis) }
        }
    }

    private fun selectTimeMillis(
        selectedTime: Long,
        values: List<Long>,
        onTimeSelected: (Long) -> Unit
    ) {
        router.setResultListener(DATE_PICKER_RESULT) { result ->
            onTimeSelected(result.safeCast() ?: 0L)
        }
        router.showDialog(
            NavScreen.DatePickerScreen(DATE_PICKER_RESULT, selectedTime, values)
        )
    }

}