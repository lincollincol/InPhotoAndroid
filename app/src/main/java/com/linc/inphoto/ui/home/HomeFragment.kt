package com.linc.inphoto.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentHomeBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.home.item.HomePostItem
import com.linc.inphoto.ui.home.item.StoriesBarItem
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.updateSingle
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.stopRefreshingDelayed
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.linc.inphoto.utils.view.recyclerview.listener.VerticalRecyclerScrollListener
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
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val storiesSection by lazy { Section() }
    private val postsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            storiesSection.updateSingle(StoriesBarItem(state.storiesUiState))
            postsSection.replaceAll(state.posts.map(::HomePostItem))
            homeProgressBar.show(state.isLoading && state.posts.isEmpty())
            notFoundLayout.root.show(!state.isLoading && state.posts.isEmpty())
            refreshLayout.stopRefreshingDelayed(!state.isLoading)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(storiesSection, postsSection)
                itemAnimator = FadeInDownAnimator()
                addOnScrollListener(VerticalRecyclerScrollListener {
                    when (it) {
                        Gravity.BOTTOM -> bottomBarViewModel.hideBottomBar()
                        Gravity.TOP -> bottomBarViewModel.showBottomBar()
                    }
                })
            }
            refreshLayout.setOnRefreshListener {
                viewModel.loadHomeData()
            }
            enterTransition = Fade(Fade.IN)
            reenterTransition = Fade(Fade.IN)
            exitTransition = Fade(Fade.OUT)
        }
        bottomBarViewModel.showBottomBar()
        viewModel.loadHomeData()
    }

}
