package com.linc.inphoto.ui.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.chats.item.ChatItem
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : BaseFragment(R.layout.fragment_chats) {

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    override val viewModel: ChatsViewModel by viewModels()
    private val binding by viewBinding(FragmentChatsBinding::bind)
    private val chatsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            chatsSection.update(state.chats.map(::ChatItem))
            progressBar.show(state.isLoading)
            notFoundLayout.root.show(!state.isLoading && state.chats.isEmpty())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            chatsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(chatsSection)
            }
        }
        viewModel.loadChats()
    }

}