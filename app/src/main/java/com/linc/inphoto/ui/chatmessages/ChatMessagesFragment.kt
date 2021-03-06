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
import com.linc.inphoto.ui.chatmessages.item.*
import com.linc.inphoto.ui.chatmessages.model.*
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator

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
            messagesSection.update(
                items = state.messages.mapNotNull {
                    when {
                        it.isIncoming && it.isTextMessage -> InTextMessageItem(it)
                        it.isIncoming && it.isImageMessage -> InImageMessageItem(it)
                        it.isIncoming && it.isVideoMessage -> InVideoMessageItem(it)
                        it.isIncoming && it.isAudioMessage -> InAudioMessageItem(it)
                        it.isTextMessage -> OutTextMessageItem(it)
                        it.isImageMessage -> OutImageMessageItem(it)
                        it.isVideoMessage -> OutVideoMessageItem(it)
                        it.isAudioMessage -> OutAudioMessageItem(it)
                        else -> null
                    }
                },
                onNewItemAdded = {
                    messagesRecyclerView.smoothScrollToStart()
                }
            )
            inputLayout.apply {
                animateTargets(Fade(), root, root.children)
                sendButton.enable(state.isMessageValid)
                doneButton.enable(state.isMessageValid)
                sendButton.show(!state.isEditorState)
                doneButton.show(state.isEditorState)
                cancelButton.show(state.isEditorState)
                attachmentsButton.show(!state.isEditorState)
                inputEditText.update(state.message)
            }
            if (state.isHideKeyboard) {
                hideKeyboard()
                viewModel.keyboardHidden()
            }
            messagesNotFoundLayout.root.show(!state.isLoading && state.messages.isEmpty())
            progressBar.show(state.isLoading && state.messages.isEmpty())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadConversation(getArgumentNotNull(CONVERSATION_ARG))
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
                addTransition(Slide(Gravity.BOTTOM).addTarget(inputLayout.root))
            }
            exitTransition = Fade(Fade.OUT)
            reenterTransition = Fade(Fade.IN)
        }
        bottomBarViewModel.hideBottomBar()
    }

}