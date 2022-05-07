package com.linc.inphoto.ui.followerslist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentFollowersListBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.followerslist.item.FollowerUserItem
import com.linc.inphoto.ui.followerslist.model.SubscriptionType
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersListFragment : BaseFragment(R.layout.fragment_followers_list) {

    companion object {
        private const val USER_ID_ARG = "user_id"
        private const val SUBSCRIPTION_TYPE_ARG = "subscription_type"

        @JvmStatic
        fun newInstance(
            userId: String?,
            type: SubscriptionType
        ) = FollowersListFragment().apply {
            arguments = bundleOf(
                USER_ID_ARG to userId,
                SUBSCRIPTION_TYPE_ARG to type
            )
        }
    }

    override val viewModel: FollowersListViewModel by viewModels()
    private val binding by viewBinding(FragmentFollowersListBinding::bind)
    private val usersAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            usersAdapter.update(state.users.map(::FollowerUserItem))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.setupContainerId(parentFragment?.safeCast<BaseFragment>()?.containerId)
        with(binding) {
            usersRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = usersAdapter
            }
        }
        viewModel.loadUsers(getArgument(USER_ID_ARG), getArgument(SUBSCRIPTION_TYPE_ARG))
    }

}