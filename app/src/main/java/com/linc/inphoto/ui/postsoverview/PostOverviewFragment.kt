package com.linc.inphoto.ui.postsoverview

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentPostOverviewBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.postsoverview.item.PostOverviewItem
import com.linc.inphoto.ui.postsoverview.model.OverviewType
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.enableItemChangeAnimation
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.linc.inphoto.utils.view.recyclerview.listener.VerticalRecyclerScrollListener
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class PostOverviewFragment : BaseFragment(R.layout.fragment_post_overview) {

    companion object {
        private const val OVERVIEW_TYPE_ARG = "overview_type"

        @JvmStatic
        fun newInstance(
            overviewType: OverviewType
        ) = PostOverviewFragment().apply {
            arguments = bundleOf(OVERVIEW_TYPE_ARG to overviewType)
        }
    }

    override val viewModel: PostOverviewViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentPostOverviewBinding::bind)
    private val postsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            postsSection.update(state.posts.map(::PostOverviewItem))
//            state.initialPost?.let {
//                val position = postsSection.getPosition(PostOverviewItem(state.initialPost))
//                postsRecyclerView.scrollToPosition(position)
//                Timber.d(position.toString())
//                viewModel.initialPostShown()
//            }
            state.initialPosition?.let {
                Timber.d(it.toString())
                postsRecyclerView.scrollToPosition(it)
                viewModel.initialPostShown()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(postsSection)
                enableItemChangeAnimation(false)
                addOnScrollListener(VerticalRecyclerScrollListener {
                    when (it) {
                        Gravity.BOTTOM -> bottomBarViewModel.hideBottomBar()
                        Gravity.TOP -> bottomBarViewModel.showBottomBar()
                    }
                })
            }
            toolbarView.apply {
                setOnCancelClickListener(viewModel::onBackPressed)
            }
        }
        bottomBarViewModel.showBottomBar()
        viewModel.applyOverviewType(getArgument(OVERVIEW_TYPE_ARG))
    }


}