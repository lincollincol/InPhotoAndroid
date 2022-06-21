package com.linc.inphoto.ui.chatattachments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatAttachmentsBinding
import com.linc.inphoto.entity.media.LocalMedia
import com.linc.inphoto.ui.base.fragment.BaseBottomSheetDialogFragment
import com.linc.inphoto.ui.chatattachments.item.AttachmentItem
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class ChatAttachmentsFragment : BaseBottomSheetDialogFragment(R.layout.fragment_chat_attachments) {

    companion object {
        private const val RESULT_KEY_ARG = "result_id"
        private const val CHAT_ID_ARG = "chat_id"
        private const val RECEIVER_ID_ARG = "receiver_id"
        private const val ATTACHMENTS_ARG = "attachments"

        @JvmStatic
        fun newInstance(
            resultKey: String,
            chatId: String?,
            receiverId: String?,
            attachments: List<LocalMedia>
        ) = ChatAttachmentsFragment().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
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
            animateTargets(Fade(), root, root.children)
            attachmentsSection.update(state.attachments.map(::AttachmentItem))
            captionEditText.update(state.captionText)
            uploadingLayout.show(state.isLoading)
            uploadLottieView.play(state.isLoading)
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
                viewModel.sendAttachmentsMessage(getArgumentNotNull(RESULT_KEY_ARG))
            }
            cancelButton.setOnThrottledClickListener {
                viewModel.cancelAttachments()
            }
            uploadingLayout.setOnClickListener {
                // Ignored
            }
        }
    }

}