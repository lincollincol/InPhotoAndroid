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
import com.linc.inphoto.ui.profile.item.NewPostItem
import com.linc.inphoto.ui.profile.item.ProfilePostItem
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.*
import com.linc.inphoto.utils.recyclerview.decorator.GridSpaceItemDecoration
import com.linc.inphoto.utils.recyclerview.listener.VerticalScrollListener
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

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
            profileNameTextField.text = state.username
            statusTectView.apply {
                text = state.status
                show(state.isValidStatus)
            }
            avatarImageView.loadImage(image = state.avatarUrl)
            headerImageView.loadImage(image = state.headerUrl)
            userPostsSection.update(state.posts.map(::ProfilePostItem))
            state.newPostUiState?.let { newPostSection.updateSingle(NewPostItem(it)) }
            animateTargets(
                Fade(),
                profileMotionLayout,
                followButton,
                messageButton,
                backButton,
            )
            followButton.show(!state.isLoggedInUser)
            messageButton.show(!state.isLoggedInUser)
            backButton.show(!state.isProfileTab)
            followersCountTextView.text = state.followersCount.toString()
            followingCountTextView.text = state.followingCount.toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalSquareGridLayoutManager(ROW_IMAGES_COUNT)
                adapter = createAdapter(hasStableIds = true, newPostSection, userPostsSection)
                enableItemChangeAnimation(false)
                addItemDecoration(
                    GridSpaceItemDecoration(
                        ROW_IMAGES_COUNT,
                        getDimension(R.dimen.margin_tiny),
                        true
                    )
                )
                addOnScrollListener(VerticalScrollListener {
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
                viewModel.onBackPressed()
            }
            reenterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.TOP).addTarget(backButton))
                addTransition(Slide(Gravity.TOP).addTarget(settingsButton))
            }
        }
        bottomBarViewModel.showBottomBar()
        viewModel.loadProfileData(getArgument(USER_ID_ARG))
    }

}