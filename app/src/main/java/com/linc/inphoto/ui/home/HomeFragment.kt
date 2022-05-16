package com.linc.inphoto.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentHomeBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.home.item.HomePostItem
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override val viewModel: HomeViewModel by viewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val postsSection by lazy { Section() }

    override suspend fun observeUiState() {
        viewModel.uiState.collect { state ->
            postsSection.update(state.posts.map(::HomePostItem))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(postsSection)
                itemAnimator = FadeInDownAnimator()
            }
        }
        viewModel.loadFollowingPosts()
    }

    override fun onTabStateChanged(hidden: Boolean) {
        super.onTabStateChanged(hidden)
        if (!hidden) viewModel.loadFollowingPosts()
    }
}