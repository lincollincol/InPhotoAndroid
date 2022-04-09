package com.linc.inphoto.ui.chats

import androidx.fragment.app.viewModels
import com.linc.inphoto.R
import com.linc.inphoto.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : BaseFragment(R.layout.fragment_chats) {

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    override val viewModel: ChatsViewModel by viewModels()

    override suspend fun observeUiState() {
    }
}