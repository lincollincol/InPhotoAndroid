package com.linc.inphoto.ui.optionpicker

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentOptionPickerBinding
import com.linc.inphoto.ui.base.fragment.BaseBottomSheetDialogFragment
import com.linc.inphoto.ui.optionpicker.item.OptionItem
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionPickerFragment : BaseBottomSheetDialogFragment(R.layout.fragment_option_picker) {

    companion object {
        private const val RESULT_KEY_ARG = "result_key"
        private const val OPTIONS_ARG = "options"

        @JvmStatic
        fun newInstance(
            resultKey: String,
            options: List<OptionModel>
        ) = OptionPickerFragment().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
                OPTIONS_ARG to options
            )
        }
    }

    val viewModel: OptionPickerViewModel by viewModels()
    private val binding by viewBinding(FragmentOptionPickerBinding::bind)

    private val optionsAdapter: GroupieAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() {
        // Empty UI state
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() = with(binding) {
        optionsRecyclerView.apply {
            layoutManager = verticalLinearLayoutManager()
            adapter = optionsAdapter
        }

        optionsLayout.setOnClickListener {
            // Clicks ignored
        }

        root.setOnClickListener {
            viewModel.selectOption(getArgument(RESULT_KEY_ARG), null)
        }

        val options = getArgument<List<OptionModel>>(OPTIONS_ARG)
        optionsAdapter.let { adapter ->
            adapter.addAll(options?.map(::OptionItem) ?: emptyList())
            adapter.setOnItemClickListener { item, _ ->
                val position = adapter.getAdapterPosition(item)
                viewModel.selectOption(getArgument(RESULT_KEY_ARG), options?.get(position))
            }
        }
    }

}