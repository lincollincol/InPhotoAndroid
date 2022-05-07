package com.linc.inphoto.ui.profilefollowers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.linc.inphoto.ui.profilefollowers.model.FollowersPageUiState

class FollowersPageAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val pages: MutableList<FollowersPageUiState> = mutableListOf()
    fun updatePages(pages: List<FollowersPageUiState>) {
        this.pages.clear()
        this.pages.addAll(pages)
        notifyDataSetChanged()
    }

    //    override fun getItemCount(): Int = SubscriptionType.values().count()
//    override fun createFragment(position: Int): Fragment {
//        return FollowersListFragment.newInstance(SubscriptionType.values()[position])
//    }
    override fun getItemCount(): Int = pages.count()
    override fun createFragment(position: Int): Fragment {
        return pages[position].screen.createFragment(fragmentManager.fragmentFactory)
    }
}