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
import com.linc.inphoto.ui.profile.item.ProfileImageItem
import com.linc.inphoto.ui.profile.model.SourceType
import com.linc.inphoto.utils.GridSpaceItemDecoration
import com.linc.inphoto.utils.extensions.getDimension
import com.linc.inphoto.utils.extensions.scrollToStart
import com.linc.inphoto.utils.extensions.verticalGridLayoutManager
import com.linc.inphoto.utils.extensions.view.THUMB_MEDIUM
import com.linc.inphoto.utils.extensions.view.loadImage
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    companion object {
        private const val ROW_IMAGES_COUNT = 3

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
                avatarImageView.loadImage(
                    image = state.user?.avatarUrl,
                    size = THUMB_MEDIUM,
                    errorPlaceholder = R.drawable.avatar_thumb
                )
            }
        }
        return@with
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeResumedLaunch {
            viewModel.loadProfileData()
        }
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalGridLayoutManager(ROW_IMAGES_COUNT)
                adapter = photosAdapter
                addItemDecoration(
                    GridSpaceItemDecoration(
                        ROW_IMAGES_COUNT,
                        getDimension(R.dimen.margin_small),
                        true
                    )
                )
            }

            photosAdapter.setOnItemClickListener { item, view ->
                viewModel.createPost()
            }

            moveUpButton.setOnClickListener {
                binding.postsRecyclerView.scrollToStart()
                binding.profileMotionLayout.transitionToStart()
            }

            avatarImageView.setOnClickListener {
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
        val photos = mutableListOf<ProfileImageItem>().apply {
            repeat(50) {
                add(ProfileImageItem())
            }
        }
        photosAdapter.addAll(photos)
    }

}