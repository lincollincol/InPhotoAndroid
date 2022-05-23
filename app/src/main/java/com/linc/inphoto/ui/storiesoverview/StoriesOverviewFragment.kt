package com.linc.inphoto.ui.storiesoverview

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentStoriesReviewBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.view.selectPage
import com.linc.inphoto.utils.view.viewpager.CubeRotationTransformer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoriesOverviewFragment : BaseFragment(R.layout.fragment_stories_review) {

    companion object {
        private const val INITIAL_USER_ID_ARG = "initial_user_id"

        @JvmStatic
        fun newInstance(initialUserId: String) = StoriesOverviewFragment().apply {
            arguments = bundleOf(INITIAL_USER_ID_ARG to initialUserId)
        }
    }

    override val viewModel: StoriesOverviewViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentStoriesReviewBinding::bind)
    private var userStoriesPageAdapter: StoriesPageAdapter? = null

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            userStoriesPageAdapter?.setPages(state.stories)
            storiesViewPager.selectPage(state.storyPosition, animate = true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userStoriesPageAdapter = StoriesPageAdapter(this)
        with(binding) {
            storiesViewPager.adapter = userStoriesPageAdapter
            storiesViewPager.setPageTransformer(CubeRotationTransformer())
        }
        bottomBarViewModel.hideBottomBar()
        viewModel.loadStories(getArgumentNotNull(INITIAL_USER_ID_ARG))
    }
}