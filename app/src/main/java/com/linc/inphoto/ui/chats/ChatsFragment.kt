package com.linc.inphoto.ui.chats

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.chats.item.ChatContactItem
import com.linc.inphoto.ui.chats.item.ChatsTitleItem
import com.linc.inphoto.ui.chats.item.ConversationItem
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.update
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class ChatsFragment : BaseFragment(R.layout.fragment_chats) {

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    override val viewModel: ChatsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentChatsBinding::bind)
    private val chatsSection by lazy { Section(ChatsTitleItem(getString(R.string.messages))) }
    private val contactsSection by lazy { Section(ChatsTitleItem(getString(R.string.contacts))) }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            searchEditText.update(state.searchQuery)
            chatsSection.update(state.chats.map(::ConversationItem))
            contactsSection.update(state.contacts.map(::ChatContactItem))
            progressBar.show(state.isLoading && state.isChatsAndContactsEmpty)
            notFoundLayout.root.show(!state.isLoading && state.isChatsAndContactsEmpty)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            chatsSection.setHideWhenEmpty(true)
            contactsSection.setHideWhenEmpty(true)
            chatsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(chatsSection, contactsSection)
                itemAnimator = FadeInDownAnimator()
            }
            searchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateSearchQuery(text.toString())
            }
        }
        bottomBarViewModel.showBottomBar()
        viewModel.loadChats()
    }

    override fun onKeyboardStateChanged(visible: Boolean) {
        super.onKeyboardStateChanged(visible)
        bottomBarViewModel.showBottomBar(!visible)
    }
}