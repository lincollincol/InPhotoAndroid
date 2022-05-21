package com.linc.inphoto.ui.profile

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.navigation.TabStateListener
import com.linc.inphoto.ui.profile.item.NewPostItem
import com.linc.inphoto.ui.profile.item.ProfilePostItem
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.*
import com.linc.inphoto.utils.view.recyclerview.decorator.GridSpaceItemDecoration
import com.linc.inphoto.utils.view.recyclerview.listener.VerticalRecyclerScrollListener
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile), TabStateListener {

    companion object {
        private const val ROW_IMAGES_COUNT = 3
        private const val USER_ID_ARG = "user_id"

        @JvmStatic
        fun newInstance(userId: String?) = ProfileFragment().apply {
            arguments = bundleOf(USER_ID_ARG to userId)
        }
    }

    override val viewModel: ProfileViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val userPostsSection: Section by lazy { Section() }
    private val newPostSection: Section by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            animateTargets(
                Fade(),
                profileMotionLayout,
                followButton,
                messageButton,
                backButton,
            )
            statusTectView.show(state.isStatusValid)
            userPostsSection.update(state.posts.map(::ProfilePostItem))
            state.newPostUiState?.let { newPostSection.updateSingle(NewPostItem(it)) }
            state.user?.let { user ->
                profileNameTextField.text = user.name
                statusTectView.text = user.status
                avatarImageView.loadImage(image = user.avatarUrl)
                headerImageView.loadImage(image = user.headerUrl)
                followButton.show(!user.isLoggedInUser && !user.isFollowingUser)
                unfollowButton.show(!user.isLoggedInUser && user.isFollowingUser)
                messageButton.show(!user.isLoggedInUser)
                followersCountTextView.text = user.followersCount.toString()
                followingCountTextView.text = user.followingCount.toString()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalSquareGridLayoutManager(ROW_IMAGES_COUNT)
                adapter = createAdapter(newPostSection, userPostsSection)
                itemAnimator = FadeInDownAnimator()
                addItemDecoration(
                    GridSpaceItemDecoration(
                        ROW_IMAGES_COUNT,
                        getDimension(R.dimen.margin_tiny),
                        true
                    )
                )
                addOnScrollListener(VerticalRecyclerScrollListener {
                    when (it) {
                        Gravity.BOTTOM -> bottomBarViewModel.hideBottomBar()
                        Gravity.TOP -> bottomBarViewModel.showBottomBar()
                    }
                })
            }
            moveUpButton.setOnThrottledClickListener {
                binding.postsRecyclerView.scrollToStart()
                binding.profileMotionLayout.transitionToStart()
            }
            settingsButton.setOnThrottledClickListener {
                viewModel.openSettings()
            }
            backButton.setOnThrottledClickListener {
                onSystemBackPressed()
            }
            followButton.setOnThrottledClickListener {
                viewModel.followUser()
            }
            unfollowButton.setOnThrottledClickListener {
                viewModel.unfollowUser()
            }
            followersLayout.setOnThrottledClickListener {
                viewModel.openFollowers()
            }
            followingLayout.setOnThrottledClickListener {
                viewModel.openFollowing()
            }
            reenterTransition = TransitionSet().apply {
                addTransition(Fade(Fade.IN).addTarget(profileDataLayout))
                addTransition(Slide(Gravity.TOP).addTarget(backButton).addTarget(settingsButton))
            }
            enterTransition = reenterTransition
        }
        bottomBarViewModel.showBottomBar()
        viewModel.loadProfileData(getArgument(USER_ID_ARG))
    }

    override fun onTabStateChanged(hidden: Boolean) {
        if (!hidden) viewModel.loadProfileData(getArgument(USER_ID_ARG))
    }

}