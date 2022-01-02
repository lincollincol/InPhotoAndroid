package com.linc.inphoto.ui.infodialog

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.DialogInfoMessageBinding
import com.linc.inphoto.ui.base.fragment.BaseStubFragment
import com.linc.inphoto.ui.base.viewmodel.BaseStubViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoMessageDialog : BaseStubFragment<DialogInfoMessageBinding, BaseStubViewModel>() {

    override val viewModel: BaseStubViewModel by viewModels()

    companion object {
        private const val TITLE_ARG = "title"
        private const val MESSAGE_ARG = "message"

        @JvmStatic
        fun newInstance(
            @StringRes title: Int,
            @StringRes message: Int
        ) = InfoMessageDialog().apply {
            arguments = bundleOf(
                TITLE_ARG to title,
                MESSAGE_ARG to message,
            )
        }
    }

    override fun getViewBinding() = DialogInfoMessageBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()

        with(binding) {
            titleTextView.setText(args.getInt(TITLE_ARG))
            messageTextView.setText(args.getInt(MESSAGE_ARG))
            gotItButton.setOnClickListener {
                viewModel.onBackPressed()
            }
        }
    }

}