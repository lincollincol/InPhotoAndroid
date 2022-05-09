package com.linc.inphoto.ui.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentChatsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : BaseFragment(R.layout.fragment_chats) {

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }

    override val viewModel: ChatsViewModel by viewModels()
    private val binding by viewBinding(FragmentChatsBinding::bind)

    override suspend fun observeUiState() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchButton.setOnThrottledClickListener {
                viewModel.loadChats()
            }
        }
    }

}