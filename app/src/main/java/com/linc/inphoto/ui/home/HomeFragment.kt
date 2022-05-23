package com.linc.inphoto.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentHomeBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override val viewModel: HomeViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val postsSection by lazy { Section() }
    private val storiesSection by lazy { Section() }
    private val newStorySection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            animateTargets(Fade(Fade.IN), contentLayout, contentLayout.children)
            postsSection.update(state.posts.map(::HomePostItem))
            storiesSection.update(state.stories.map(::UserStoryItem))
            state.newStory?.let { newStorySection.updateSingle(NewStoryItem(it)) }
            storiesRecyclerView.show(state.newStory != null)
            separatorView.show(state.newStory != null)
            homeProgressBar.show(state.isLoading && state.posts.isEmpty())
            notFoundLayout.root.show(!state.isLoading && state.posts.isEmpty())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            storiesRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = createAdapter(newStorySection, storiesSection)
                itemAnimator = FadeInLeftAnimator()
            }
            postsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(postsSection)
                itemAnimator = FadeInDownAnimator()
            }
            contentScrollView.setOnScrollChangeListener(VerticalNestedScrollListener {
                when (it) {
                    Gravity.BOTTOM -> bottomBarViewModel.hideBottomBar()
                    Gravity.TOP -> bottomBarViewModel.showBottomBar()
                }
            })
        }
        viewModel.loadHomeData()
        bottomBarViewModel.showBottomBar()
    }

    override fun onTabStateChanged(hidden: Boolean) {
        super.onTabStateChanged(hidden)
        if (!hidden) viewModel.loadHomeData()
    }
}