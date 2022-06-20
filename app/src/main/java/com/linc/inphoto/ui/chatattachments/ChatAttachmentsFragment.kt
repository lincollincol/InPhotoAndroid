package com.linc.inphoto.ui.chatattachments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatAttachmentsBinding
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.fragment.BaseBottomSheetDialogFragment
import com.linc.inphoto.ui.chatattachments.item.AttachmentItem
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.update
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class ChatAttachmentsFragment : BaseBottomSheetDialogFragment(R.layout.fragment_chat_attachments) {

    companion object {
        private const val CHAT_ID_ARG = "chat_id"
        private const val RECEIVER_ID_ARG = "receiver_id"
        private const val ATTACHMENTS_ARG = "attachments"

        @JvmStatic
        fun newInstance(
            chatId: String?,
            receiverId: String?,
            attachments: List<LocalMedia>
        ) = ChatAttachmentsFragment().apply {
            arguments = bundleOf(
                CHAT_ID_ARG to chatId,
                RECEIVER_ID_ARG to receiverId,
                ATTACHMENTS_ARG to attachments
            )
        }
    }

    override val viewModel: ChatAttachmentsViewModel by viewModels()
    private val binding by viewBinding(FragmentChatAttachmentsBinding::bind)
    private val attachmentsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            attachmentsSection.update(state.attachments.map(::AttachmentItem))
            captionEditText.update(state.captionText)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyAttachments(
            getArgument(CHAT_ID_ARG),
            getArgument(RECEIVER_ID_ARG),
            getArgumentNotNull(ATTACHMENTS_ARG)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            attachmentsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(attachmentsSection)
                itemAnimator = FadeInDownAnimator()
            }
            captionEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateCation(text.toString())
            }
            sendButton.setOnThrottledClickListener {
                viewModel.sendAttachmentsMessage()
            }
            cancelButton.setOnThrottledClickListener {
                viewModel.cancelAttachments()
            }
        }
    }

}