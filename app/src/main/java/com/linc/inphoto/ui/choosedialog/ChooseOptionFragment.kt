package com.linc.inphoto.ui.choosedialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.DialogChooseOptionBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.choosedialog.item.ChooseOptionItem
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.utils.extensions.verticalLinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseOptionFragment : BaseFragment(R.layout.dialog_choose_option) {

    companion object {
        private const val OPTIONS_ARG = "options"

        @JvmStatic
        fun newInstance(
            options: List<ChooseOptionModel>
        ) = ChooseOptionFragment().apply {
            arguments = bundleOf(OPTIONS_ARG to options)
        }
    }

    override val viewModel: ChooseDialogViewModel by viewModels()
    private val binding by viewBinding(DialogChooseOptionBinding::bind)

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

        val options = requireArguments().getParcelableArrayList<ChooseOptionModel>(OPTIONS_ARG)
        optionsAdapter.let { adapter ->
            adapter.addAll(options?.map { ChooseOptionItem(it) } ?: emptyList())
            adapter.setOnItemClickListener { item, _ ->
                val position = adapter.getAdapterPosition(item)
                viewModel.onFinishWithResult(position)
            }
        }
    }

}