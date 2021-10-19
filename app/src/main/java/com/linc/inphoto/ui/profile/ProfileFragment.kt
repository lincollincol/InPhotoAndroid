package com.linc.inphoto.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileBinding
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.profile.item.ProfilePhotoItem
import com.linc.inphoto.utils.SpaceItemDecoration
import com.linc.inphoto.utils.extensions.getDimension
import com.linc.inphoto.utils.extensions.scrollToStart
import com.linc.inphoto.utils.extensions.smoothScrollToStart
import com.linc.inphoto.utils.extensions.verticalGridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel, ProfileUiState, BaseUiEffect>() {

    override val viewModel: ProfileViewModel by viewModels()
    private var photosAdapter: GroupieAdapter? = null

    companion object {
        private const val LIST_PHOTOS_SPAN_COUNT = 3

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun handleUiState(state: ProfileUiState) = when(state) {
        is ProfileUiState.UpdateUserData -> {
            binding.profileNameTextField.text = state.userModel.name
        }
    }

    override fun handleUiEffect(effect: BaseUiEffect) = when(effect) {
        is BaseUiEffect.Loading -> {}
        is BaseUiEffect.Error -> showErrorMessage(effect.message)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeResumedLaunch {
            viewModel.getUserData()
        }
        initUi()
    }

    private fun initUi() {
        photosAdapter = GroupieAdapter()
        binding.profilePhotosList.apply {
            layoutManager = verticalGridLayoutManager(LIST_PHOTOS_SPAN_COUNT)
            adapter = photosAdapter
            /*addItemDecoration(SpaceItemDecoration(
                getDimension(R.dimen.margin_all_general_small),
                getDimension(R.dimen.margin_all_general)
            ))*/
        }

        binding.moveUpButton.setOnClickListener {
            binding.profilePhotosList.scrollToStart()
            binding.profileMotionLayout.transitionToStart()
        }

        mockPhotos()
    }

    private fun mockPhotos() {
        val photos = mutableListOf<ProfilePhotoItem>().apply {
            repeat(50) {
                add(ProfilePhotoItem())
            }
        }
        photosAdapter?.addAll(photos)
    }

}