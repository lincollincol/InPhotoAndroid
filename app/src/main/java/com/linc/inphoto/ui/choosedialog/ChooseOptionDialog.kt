package com.linc.inphoto.ui.choosedialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.DialogChooseOptionBinding
import com.linc.inphoto.ui.base.fragment.BaseStubFragment
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import com.linc.inphoto.ui.choosedialog.item.ChooseOptionItem
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.utils.extensions.verticalLinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseOptionDialog : BaseStubFragment<DialogChooseOptionBinding, ChooseDialogViewModel>() {

    override val viewModel: ChooseDialogViewModel by viewModels()
    private var optionsAdapter: GroupieAdapter? = null

    companion object {
        private const val OPTIONS_ARG = "options"

        @JvmStatic
        fun newInstance(
            options: List<ChooseOptionModel>
        ) = ChooseOptionDialog().apply {
            arguments = bundleOf(OPTIONS_ARG to options,)
        }
    }

    override fun getViewBinding() = DialogChooseOptionBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        optionsAdapter = GroupieAdapter()

        with(binding) {
            optionsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = optionsAdapter
            }
        }

        val options = requireArguments().getParcelableArrayList<ChooseOptionModel>(OPTIONS_ARG)
        optionsAdapter?.let { adapter ->
            adapter.addAll(options?.map { ChooseOptionItem(it) } ?: emptyList())
            adapter.setOnItemClickListener { item, _ ->
                val position = adapter.getAdapterPosition(item)
                viewModel.onFinishWithResult(position)
            }
        }
    }

}