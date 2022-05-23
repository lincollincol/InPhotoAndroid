package com.linc.inphoto.ui.profilefollowers

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentFollowersListBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.profilefollowers.item.FollowerUserItem
import com.linc.inphoto.ui.profilefollowers.model.SubscriptionType
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
class FollowersListFragment : BaseFragment(R.layout.fragment_followers_list) {

    companion object {
        private const val SUBSCRIPTION_TYPE_ARG = "subscription_type"

        @JvmStatic
        fun newInstance(type: SubscriptionType) = FollowersListFragment().apply {
            arguments = bundleOf(SUBSCRIPTION_TYPE_ARG to type)
        }
    }

    override val viewModel: ProfileFollowersViewModel by viewModels(::requireParentFragment)
    private val binding by viewBinding(FragmentFollowersListBinding::bind)
    private val usersSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            val items = when (getArgument<SubscriptionType>(SUBSCRIPTION_TYPE_ARG)) {
                SubscriptionType.FOLLOWER -> state.followers
                else -> state.following
            }.map(::FollowerUserItem)
            usersSection.update(items)
            animateTargets(Fade(Fade.IN), root, notFoundLayout.root, progressBar)
            notFoundLayout.root.show(!state.isLoading && items.isEmpty())
            progressBar.show(state.isLoading && items.isEmpty())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            usersRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(true, usersSection)
                itemAnimator = FadeInDownAnimator()
            }
        }
    }

}