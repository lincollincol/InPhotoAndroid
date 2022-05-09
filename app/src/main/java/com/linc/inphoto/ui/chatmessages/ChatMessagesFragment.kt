package com.linc.inphoto.ui.chatmessages

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatMessagesBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.chatmessages.item.IncomingMessageItem
import com.linc.inphoto.ui.chatmessages.item.OutcomingMessageItem
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.enable
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.update
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
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

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            messagesSection.update(state.messages.map {
                when {
                    it.isIncoming -> IncomingMessageItem(it)
                    else -> OutcomingMessageItem(it)
//                    else -> IncomingMessageItem(it)
                }
            })
            progressBar.show(state.isLoading)
            inputLayout.apply {
                animateTargets(
                    Fade(),
                    inputLayout.root,
                    sendButton,
                    doneButton,
                    attachmentsButton,
                    cancelButton,
                    inputEditText
                )
                sendButton.enable(state.isMessageValid)
                doneButton.enable(state.isMessageValid)
                sendButton.show(!state.isEditorState)
                doneButton.show(state.isEditorState)
                attachmentsButton.show(!state.isEditorState)
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
        }
        bottomBarViewModel.hideBottomBar()
        viewModel.loadChatMessages(getArgument(CHAT_ID_ARG))
    }

}