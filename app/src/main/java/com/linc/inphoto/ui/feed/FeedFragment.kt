package com.linc.inphoto.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentFeedBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment(R.layout.fragment_feed) {

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }

    override val viewModel: FeedViewModel by viewModels()
    private val binding by viewBinding(FragmentFeedBinding::bind)

    override suspend fun observeUiState() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchButton.setOnThrottledClickListener {
                viewModel.search()
            }
        }
    }

}