package com.linc.inphoto.ui.optionpicker

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentOptionPickerBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.optionpicker.item.OptionItem
import com.linc.inphoto.ui.optionpicker.model.OptionModel
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.verticalLinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OptionPickerFragment : BaseFragment(R.layout.fragment_option_picker) {

    companion object {
        private const val OPTIONS_ARG = "options"

        @JvmStatic
        fun newInstance(
            options: List<OptionModel>
        ) = OptionPickerFragment().apply {
            arguments = bundleOf(OPTIONS_ARG to options)
        }
    }

    override val viewModel: OptionPickerViewModel by viewModels()
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
            viewModel.onFinishWithResult(null)
        }

        val options = getArgument<List<OptionModel>>(OPTIONS_ARG)
        optionsAdapter.let { adapter ->
            adapter.addAll(options?.map(::OptionItem) ?: emptyList())
            adapter.setOnItemClickListener { item, _ ->
                val position = adapter.getAdapterPosition(item)
                viewModel.onFinishWithResult(options?.get(position))
            }
        }
    }

}