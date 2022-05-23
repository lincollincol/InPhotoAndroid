package com.linc.inphoto.ui.infodialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentInfoMessageBinding
import com.linc.inphoto.ui.base.fragment.BaseBottomSheetDialogFragment
import com.linc.inphoto.utils.extensions.getArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoMessageFragment : BaseBottomSheetDialogFragment(R.layout.fragment_info_message) {
    companion object {
        private const val RESULT_KEY_ARG = "result_key"
        private const val TITLE_ARG = "title"
        private const val MESSAGE_ARG = "message"

        @JvmStatic
        fun newInstance(
            resultKey: String?,
            title: String,
            message: String
        ) = InfoMessageFragment().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
                TITLE_ARG to title,
                MESSAGE_ARG to message,
            )
        }
    }


    override val viewModel: InfoMessageViewModel by viewModels()
    private val binding by viewBinding(FragmentInfoMessageBinding::bind)

    override suspend fun observeUiState() {
        // Empty UI State
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            titleTextView.text = getArgument(TITLE_ARG)
            infoTextView.text = getArgument(MESSAGE_ARG)
            gotItButton.setOnClickListener {
                viewModel.finishInfo(getArgument(RESULT_KEY_ARG))
            }
        }
    }

}