package com.linc.inphoto.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSearchBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.ui.search.model.SearchType
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.*
import com.linc.inphoto.utils.view.TabPositionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    override val viewModel: SearchViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentSearchBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            searchEditText.update(state.searchQuery)
            searchViewPager.selectPage(state.selectedPage)
            searchTabLayout.selectTab(state.selectedPage)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchEditText.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) hideKeyboard()
                }
                doOnTextChanged { text, _, _, _ ->
                    viewModel.updateSearchQuery(text.toString())
                }
            }
            searchViewPager.apply {
                adapter = SearchPageAdapter(this@SearchFragment)
                offscreenPageLimit = SubscriptionType.values().count()
            }
            searchTabLayout.apply {
                attachMediator(searchViewPager) { tab, position ->
                    val title = when (SearchType.values().get(position)) {
                        SearchType.USERS -> R.string.users
                        SearchType.TAGS -> R.string.tags
                    }
                    tab.setText(title)
                }
                // Add listener after mediator to prevent events
                addOnTabSelectedListener(TabPositionListener(viewModel::selectPage))
            }
        }
        enterTransition = Fade(Fade.IN)
        reenterTransition = enterTransition
        bottomBarViewModel.showBottomBar()
    }

    override fun onKeyboardStateChanged(visible: Boolean) {
        bottomBarViewModel.showBottomBar(!visible)
    }

}