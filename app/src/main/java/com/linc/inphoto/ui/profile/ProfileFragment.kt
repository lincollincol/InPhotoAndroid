package com.linc.inphoto.ui.profile

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.profile.item.NewPostItem
import com.linc.inphoto.ui.profile.item.ProfilePostItem
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getDimension
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
            profileNameTextField.text = state.user?.name
            statusTectView.apply {
                text = state.user?.status
                show(state.isValidStatus)
            }
            avatarImageView.loadImage(image = state.user?.avatarUrl)
            headerImageView.loadImage(image = state.user?.headerUrl)
            userPostsSection.update(state.posts.map(::ProfilePostItem))
            state.newPostUiState?.let { newPostSection.update(listOf(NewPostItem(it))) }
            followButton.show(false)
            messageButton.show(false)
        }
        return@with
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

            moveUpButton.setOnClickListener {
                binding.postsRecyclerView.scrollToStart()
                binding.profileMotionLayout.transitionToStart()
            }

            settingsButton.setOnClickListener {
                viewModel.openSettings()
            }
        }
        bottomBarViewModel.showBottomBar()
        viewModel.loadProfileData()
    }

}