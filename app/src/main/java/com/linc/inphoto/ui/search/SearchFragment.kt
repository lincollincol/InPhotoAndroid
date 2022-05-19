package com.linc.inphoto.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSearchBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.ui.search.model.SearchType
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.setTabSelected
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
            searchTabLayout.setTabSelected(state.selectedPage)
            searchViewPager.setCurrentItem(state.selectedPage, false)
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
            TabLayoutMediator(searchTabLayout, searchViewPager) { tab, position ->
                val title = when (SearchType.values().get(position)) {
                    SearchType.USERS -> R.string.users
                    SearchType.TAGS -> R.string.tags
                }
                tab.setText(title)
            }.attach()
        }
        bottomBarViewModel.showBottomBar()
    }

    override fun onStop() {
        super.onStop()
        viewModel.selectPage(binding.searchViewPager.currentItem)
    }

    override fun onKeyboardStateChanged(visible: Boolean) {
        bottomBarViewModel.showBottomBar(!visible)
    }

}