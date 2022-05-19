package com.linc.inphoto.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.linc.inphoto.ui.search.model.SearchType

class SearchPageAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = SearchType.values().count()
    override fun createFragment(position: Int): Fragment {
        return SearchResultFragment.newInstance(SearchType.values()[position])
    }
}