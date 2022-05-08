package com.linc.inphoto.ui.profilefollowers

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileFollowersBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFollowersFragment : BaseFragment(R.layout.fragment_profile_followers) {

    companion object {
        private const val USER_ID_ARG = "user_id"

        @JvmStatic
        fun newInstance(userId: String?) = ProfileFollowersFragment().apply {
            arguments = bundleOf(USER_ID_ARG to userId)
        }
    }

    override val viewModel: ProfileFollowersViewModel by viewModels()
    private val binding by viewBinding(FragmentProfileFollowersBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            followersToolbar.setToolbarTitle(state.user?.name)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            followersToolbar.setOnCancelClickListener { viewModel.onBackPressed() }
            followersViewPager.apply {
                adapter = FollowersPageAdapter(this@ProfileFollowersFragment)
                offscreenPageLimit = SubscriptionType.values().count()
            }
            TabLayoutMediator(followersTabLayout, followersViewPager) { tab, position ->
                val title = when (SubscriptionType.values().get(position)) {
                    SubscriptionType.FOLLOWER -> R.string.followers
                    SubscriptionType.FOLLOWING -> R.string.following
                }
                tab.setText(title)
            }.attach()
        }
        viewModel.loadUserSubscriptions(getArgument(USER_ID_ARG))
    }

}