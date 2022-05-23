package com.linc.inphoto.ui.datepicker

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentDurationPickerBinding
import com.linc.inphoto.ui.base.fragment.BaseBottomSheetDialogFragment
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.setValues
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DurationPickerFragment : BaseBottomSheetDialogFragment(R.layout.fragment_duration_picker) {

    companion object {
        private const val RESULT_KEY_ARG = "result_key"
        private const val TITLE_ARG = "title"
        private const val SELECTED_DURATION_ARG = "selected_duration"
        private const val DURATION_OPTIONS_ARG = "duration_options"

        @JvmStatic
        fun newInstance(
            resultKey: String,
            title: String,
            selectedDuration: Long,
            durationMillisOptions: List<Long>
        ) = DurationPickerFragment().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
                TITLE_ARG to title,
                SELECTED_DURATION_ARG to selectedDuration,
                DURATION_OPTIONS_ARG to durationMillisOptions,
            )
        }
    }

    override val viewModel: DurationPickerViewModel by viewModels()
    private val binding by viewBinding(FragmentDurationPickerBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            durationPicker.apply {
                setValues(
                    state.valuesMillis.map { DateFormatter.getDuration(requireContext(), it) }
                )
                value = state.selectedValue
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyPickerConfig(
            getArgumentNotNull(SELECTED_DURATION_ARG),
            getArgumentNotNull(DURATION_OPTIONS_ARG)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            titleTextView.text = getArgument(TITLE_ARG)
            durationPicker.setOnValueChangedListener { _, _, position ->
                viewModel.selectValue(position)
            }
            confirmButton.setOnThrottledClickListener {
                viewModel.confirmValue(getArgument(RESULT_KEY_ARG))
            }
            cancelButton.setOnThrottledClickListener {
                viewModel.cancelPicker()
            }
        }
    }
}