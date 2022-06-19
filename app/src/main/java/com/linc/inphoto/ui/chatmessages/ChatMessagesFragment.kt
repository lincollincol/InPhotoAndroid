package com.linc.inphoto.ui.chatmessages

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatMessagesBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.chatmessages.item.IncomingMessageItem
import com.linc.inphoto.ui.chatmessages.item.MessageAttachmentItem
import com.linc.inphoto.ui.chatmessages.item.OutcomingMessageItem
import com.linc.inphoto.ui.chatmessages.model.ConversationParams
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator
import timber.log.Timber

@AndroidEntryPoint
class ChatMessagesFragment : BaseFragment(R.layout.fragment_chat_messages) {

    companion object {
        private const val CONVERSATION_ARG = "conversation_id"

        @JvmStatic
        fun newInstance(conversation: ConversationParams) = ChatMessagesFragment().apply {
            arguments = bundleOf(CONVERSATION_ARG to conversation)
        }
    }

    override val viewModel: ChatMessagesViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentChatMessagesBinding::bind)
    private val messagesSection by lazy { Section() }
    private val attachmentsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            animateTargets(Fade(), root, root.children)
            chatToolbarView.apply {
                setToolbarTitle(state.username)
                loadAvatarImage(state.userAvatarUrl)
            }
            attachmentsSection.update(state.messageAttachments.map(::MessageAttachmentItem))
            messagesSection.update(state.messages.map {
                when {
                    it.isIncoming -> IncomingMessageItem(it)
                    else -> OutcomingMessageItem(it)
                }
            })
            inputLayout.apply {
                animateTargets(Fade(), root, root.children)
                attachmentsRecyclerView.show(state.hasAttachments)
                sendButton.enable(state.isMessageValid)
                doneButton.enable(state.isMessageValid)
                sendButton.show(!state.isEditorState)
                doneButton.show(state.isEditorState)
                cancelButton.show(state.isEditorState)
                inputEditText.update(state.message)
            }
            if (state.isScrollDownOnUpdate) messagesRecyclerView.smoothScrollToStart()
            messagesNotFoundLayout.root.show(!state.isLoading && state.messages.isEmpty())
            progressBar.show(state.isLoading && state.messages.isEmpty())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadConversation(getArgumentNotNull(CONVERSATION_ARG))
        Timber.tag("CHAT_VM_REF").d(viewModel.toString())
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
                messagesNotFoundLayout.apply {
                    notFoundImageView.setImageResource(R.drawable.ic_chat)
                    notFoundTextView.setText(R.string.no_messages)
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
                chatToolbarView.apply {
                    setOnTitleClickListener { viewModel.selectUserProfile() }
                    setOnImageClickListener { viewModel.selectUserProfile() }
                    setOnCancelClickListener { viewModel.onBackPressed() }
                }
            }
            enterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.TOP).addTarget(chatToolbarView))
                addTransition(Slide(Gravity.BOTTOM).addTarget(inputLayout.root))
            }
            reenterTransition = Fade(Fade.IN)
        }
        bottomBarViewModel.hideBottomBar()
    }

}