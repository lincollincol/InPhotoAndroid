package com.linc.inphoto.ui.feed

import androidx.fragment.app.viewModels
import com.linc.inphoto.R
import com.linc.inphoto.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment(R.layout.fragment_feed) {

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }

    override val viewModel: FeedViewModel by viewModels()

    override suspend fun observeUiState() {
    }
}