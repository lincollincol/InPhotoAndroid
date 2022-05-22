package com.linc.inphoto.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentHomeBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.home.item.HomePostItem
import com.linc.inphoto.ui.home.item.NewStoryItem
import com.linc.inphoto.ui.home.item.UserStoryItem
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.updateSingle
import com.linc.inphoto.utils.extensions.view.horizontalLinearLayoutManager
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override val viewModel: HomeViewModel by viewModels()
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
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            storiesRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = createAdapter(newStorySection, storiesSection)
                itemAnimator = FadeInRightAnimator()
            }
            postsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(postsSection)
                itemAnimator = FadeInDownAnimator()
            }
        }
        viewModel.loadHomeData()
    }

    override fun onTabStateChanged(hidden: Boolean) {
        super.onTabStateChanged(hidden)
        if (!hidden) viewModel.loadHomeData()
    }
}