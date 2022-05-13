package com.linc.inphoto.ui.profilefollowers

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileFollowersBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.setTabSelected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFollowersFragment : BaseFragment(R.layout.fragment_profile_followers) {

    companion object {
        private const val USER_ID_ARG = "user_id"
        private const val SUBSCRIPTION_TYPE_ARG = "subscription_type"

        @JvmStatic
        fun newInstance(
            userId: String?,
            subscriptionType: SubscriptionType
        ) = ProfileFollowersFragment().apply {
            arguments = bundleOf(
                USER_ID_ARG to userId,
                SUBSCRIPTION_TYPE_ARG to subscriptionType
            )
        }
    }

    override val viewModel: ProfileFollowersViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentProfileFollowersBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            followersToolbar.setToolbarTitle(state.user?.name)
            followersTabLayout.setTabSelected(state.selectedPage)
            followersViewPager.setCurrentItem(state.selectedPage, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUserSubscriptions(
            getArgument(USER_ID_ARG),
            getArgument(SUBSCRIPTION_TYPE_ARG)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            followersToolbar.setOnCancelClickListener { viewModel.onBackPressed() }
            searchEditText.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) hideKeyboard()
                }
                doOnTextChanged { text, _, _, _ ->
                    viewModel.updateSearchQuery(text.toString())
                }
            }
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
        bottomBarViewModel.showBottomBar()
    }

    override fun onStop() {
        super.onStop()
        viewModel.selectPage(binding.followersViewPager.currentItem)
    }

    override fun onKeyboardStateChanged(visible: Boolean) {
        bottomBarViewModel.showBottomBar(!visible)
    }
}