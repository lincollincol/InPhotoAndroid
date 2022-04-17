package com.linc.inphoto.ui.confirmdialog

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.DialogConfirmBinding
import com.linc.inphoto.ui.base.fragment.BaseBottomSheetDialogFragment
import com.linc.inphoto.utils.extensions.getArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmDialog : BaseBottomSheetDialogFragment(R.layout.dialog_confirm) {

    companion object {
        private const val RESULT_KEY_ARG = "result_key"
        private const val TITLE_ARG = "title"
        private const val DESCRIPTION_ARG = "description"

        @JvmStatic
        fun newInstance(
            resultKey: String?,
            @StringRes title: Int,
            @StringRes description: Int
        ) = ConfirmDialog().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
                TITLE_ARG to title,
                DESCRIPTION_ARG to description
            )
        }
    }

    override val viewModel: ConfirmViewModel by viewModels()
    private val binding by viewBinding(DialogConfirmBinding::bind)

    override suspend fun observeUiState() {
        // Empty state
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyResultKey(getArgument(RESULT_KEY_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            titleTextView.setText(getArgument(TITLE_ARG, 0))
            descriptionTextView.setText(getArgument(DESCRIPTION_ARG, 0))

            confirmButton.setOnClickListener {
                viewModel.confirm()
            }
            cancelButton.setOnClickListener {
                viewModel.cancel()
            }
        }
    }
}