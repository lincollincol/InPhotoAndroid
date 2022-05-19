package com.linc.inphoto.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSearchResultBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.search.item.SearchTagItem
import com.linc.inphoto.ui.search.item.SearchUserItem
import com.linc.inphoto.ui.search.model.SearchType
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class SearchResultFragment : BaseFragment(R.layout.fragment_search_result) {

    companion object {
        private const val SEARCH_TYPE_ARG = "search_type"

        @JvmStatic
        fun newInstance(type: SearchType) = SearchResultFragment().apply {
            arguments = bundleOf(SEARCH_TYPE_ARG to type)
        }
    }

    override val viewModel: SearchViewModel by viewModels({ parentFragment as Fragment })
    private val binding by viewBinding(FragmentSearchResultBinding::bind)
    private val resultsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            val items = when (getArgument<SearchType>(SEARCH_TYPE_ARG)) {
                SearchType.USERS -> state.users.map(::SearchUserItem)
                else -> state.tags.map(::SearchTagItem)
            }
            resultsSection.update(items)
            animateTargets(Fade(Fade.IN), root, notFoundLayout.root, progressBar)
            notFoundLayout.root.show(!state.isLoading && items.isEmpty())
            progressBar.show(state.isLoading && items.isEmpty())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            resultsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(resultsSection)
                itemAnimator = FadeInDownAnimator()
            }
        }
    }

}