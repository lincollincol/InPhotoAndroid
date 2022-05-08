package com.linc.inphoto.ui.profilefollowers

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType

class FollowersPageAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = SubscriptionType.values().count()
    override fun createFragment(position: Int): Fragment {
        return FollowersListFragment.newInstance(SubscriptionType.values()[position])
    }
}