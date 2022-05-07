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
import com.linc.inphoto.ui.followerslist.model.SubscriptionType
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.setTabTitle
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
    private var pageAdapter: FollowersPageAdapter? = null
//    private val pageAdapter by lazy { FollowersPageAdapter(parentFragmentManager, lifecycle) }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            followersToolbar.setToolbarTitle(state.user?.name)
            pageAdapter?.updatePages(state.pages)
            state.pages.forEachIndexed { index, followersPageUiState ->
                followersTabLayout.setTabTitle(index, followersPageUiState.title)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUserSubscriptions(getArgument(USER_ID_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            pageAdapter = FollowersPageAdapter(childFragmentManager, lifecycle)
            followersToolbar.setOnCancelClickListener { viewModel.onBackPressed() }
            followersViewPager.apply {
                adapter = pageAdapter
                offscreenPageLimit = SubscriptionType.values().count()
            }
            TabLayoutMediator(followersTabLayout, followersViewPager) { tab, position ->
            }.attach()
        }
    }

}