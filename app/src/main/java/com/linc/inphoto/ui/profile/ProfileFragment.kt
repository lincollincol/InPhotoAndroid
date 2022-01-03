package com.linc.inphoto.ui.profile

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.choosedialog.model.ChooseOptionModel
import com.linc.inphoto.ui.profile.item.ProfilePhotoItem
import com.linc.inphoto.utils.extensions.scrollToStart
import com.linc.inphoto.utils.extensions.verticalGridLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    companion object {
        private const val LIST_PHOTOS_SPAN_COUNT = 3

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override val viewModel: ProfileViewModel by viewModels()

    override val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val photosAdapter: GroupieAdapter by lazy { GroupieAdapter() }

    private val updateAvatarPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val options = listOf(R.string.camera, R.string.gallery)
            .zip(permissions.values)
            .map { ChooseOptionModel(it.first, it.second) }

        viewModel.onUpdateAvatar(options)
    }


    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            profileNameTextField.text = state.user?.name

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeResumedLaunch {
            viewModel.loadProfileData()
        }
        initUi()
    }

    private fun initUi() {
        with(binding) {
            profilePhotosList.apply {
                layoutManager = verticalGridLayoutManager(LIST_PHOTOS_SPAN_COUNT)
                adapter = photosAdapter
                /*addItemDecoration(SpaceItemDecoration(
                    getDimension(R.dimen.margin_all_general_small),
                    getDimension(R.dimen.margin_all_general)
                ))*/
            }

            moveUpButton.setOnClickListener {
                binding.profilePhotosList.scrollToStart()
                binding.profileMotionLayout.transitionToStart()
            }

            profileAvatarImage.setOnClickListener {
                updateAvatarPermissions.launch(arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ))
            }
        }
        mockPhotos()
    }

    private fun mockPhotos() {
        val photos = mutableListOf<ProfilePhotoItem>().apply {
            repeat(50) {
                add(ProfilePhotoItem())
            }
        }
        photosAdapter.addAll(photos)
    }

}