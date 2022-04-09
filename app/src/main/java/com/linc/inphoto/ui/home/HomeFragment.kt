package com.linc.inphoto.ui.home

import androidx.fragment.app.viewModels
import com.linc.inphoto.R
import com.linc.inphoto.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override val viewModel: HomeViewModel by viewModels()

    override suspend fun observeUiState() {
    }
}