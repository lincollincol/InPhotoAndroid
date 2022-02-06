package com.linc.inphoto.ui.profile

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.profile.item.ProfilePhotoItem
import com.linc.inphoto.ui.profile.model.SourceType
import com.linc.inphoto.utils.extensions.scrollToStart
import com.linc.inphoto.utils.extensions.verticalGridLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        private const val LIST_PHOTOS_SPAN_COUNT = 3

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override val viewModel: ProfileViewModel by viewModels()

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val photosAdapter: GroupieAdapter by lazy { GroupieAdapter() }

    private val imagePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val source = permissions.map { permission ->
            when (permission.key) {
                Manifest.permission.CAMERA -> SourceType.Camera(permission.value)
                else -> SourceType.Gallery(permission.value)
            }
        }
        viewModel.selectImageSource(source)
    }

    override suspend fun observeUiState() = with(binding) {
        safeResumedLaunch {
            viewModel.uiState.collect { state ->
                profileNameTextField.text = state.user?.name

            }
        }
        /*safeResumedLaunch {
            viewModel.selectGalleryImageEvent.filterNotNull().collect {
                Timber.d("Open gallery")
            }
        }
        safeResumedLaunch {
            viewModel.selectCameraImageEvent.filterNotNull().collect {
                Timber.d("Open camera")
            }
        }*/
        return@with
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
                imagePermissions.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
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