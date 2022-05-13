package com.linc.inphoto.ui.chatmessages

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatMessagesBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.chatmessages.item.IncomingMessageItem
import com.linc.inphoto.ui.chatmessages.item.MessageAttachmentItem
import com.linc.inphoto.ui.chatmessages.item.OutcomingMessageItem
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator

@AndroidEntryPoint
class ChatMessagesFragment : BaseFragment(R.layout.fragment_chat_messages) {

    companion object {
        private const val CHAT_ID_ARG = "chat_id"

        @JvmStatic
        fun newInstance(chatId: String) = ChatMessagesFragment().apply {
            arguments = bundleOf(CHAT_ID_ARG to chatId)
        }
    }

    override val viewModel: ChatMessagesViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentChatMessagesBinding::bind)
    private val messagesSection by lazy { Section() }
    private val attachmentsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            messagesSection.update(state.messages.map {
                when {
                    it.isIncoming -> IncomingMessageItem(it)
                    else -> OutcomingMessageItem(it)
                }
            })
            if (state.isScrollDownOnUpdate) messagesRecyclerView.smoothScrollToStart()
            progressBar.show(state.isLoading)
            attachmentsSection.update(state.messageAttachments.map(::MessageAttachmentItem))
            inputLayout.apply {
                animateTargets(Fade(), root, root.children)
                attachmentsRecyclerView.show(state.hasAttachments)
                sendButton.enable(state.isMessageValid)
                doneButton.enable(state.isMessageValid)
                sendButton.show(!state.isEditorState)
                doneButton.show(state.isEditorState)
//                attachmentsButton.show(!state.isEditorState)
                cancelButton.show(state.isEditorState)
                inputEditText.update(state.message)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            messagesRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager(true)
                adapter = createAdapter(messagesSection)
                itemAnimator = FadeInUpAnimator()
            }
            inputLayout.apply {
                attachmentsRecyclerView.apply {
                    layoutManager = horizontalLinearLayoutManager()
                    adapter = createAdapter(attachmentsSection)
                    itemAnimator = FadeInUpAnimator()
                }
                inputEditText.doOnTextChanged { text, _, _, _ ->
                    viewModel.updateMessage(text.toString())
                }
                sendButton.setOnThrottledClickListener {
                    viewModel.sendMessage()
                }
                doneButton.setOnThrottledClickListener {
                    viewModel.updateMessage()
                }
                cancelButton.setOnThrottledClickListener {
                    viewModel.cancelMessageEditor()
                }
                attachmentsButton.setOnThrottledClickListener {
                    viewModel.selectAttachments()
                }
            }
        }
        bottomBarViewModel.hideBottomBar()
        viewModel.loadChatMessages(getArgument(CHAT_ID_ARG))
    }

}