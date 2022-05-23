package com.linc.inphoto.ui.gallery

import android.Manifest
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentGalleryBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.gallery.item.GalleryImageItem
import com.linc.inphoto.ui.gallery.model.GalleryIntent
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.getDimension
import com.linc.inphoto.utils.extensions.permissionDisabled
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalSquareGridLayoutManager
import com.linc.inphoto.utils.view.recyclerview.decorator.GridSpaceItemDecoration
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery) {

    companion object {
        private const val ROW_IMAGES_COUNT = 3
        private const val INTENT_ARG = "intent_key"

        @JvmStatic
        fun newInstance(intent: GalleryIntent) = GalleryFragment().apply {
            arguments = bundleOf(INTENT_ARG to intent)
        }
    }

    override val viewModel: GalleryViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentGalleryBinding::bind)
    private val imagesAdapter by lazy { GroupieAdapter() }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        when {
            allowed -> viewModel.loadImages(getArgument(INTENT_ARG))
            else -> viewModel.permissionDenied()
        }
    }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            imagesAdapter.replaceAll(state.images.map(::GalleryImageItem))
            imagesRecyclerView.show(state.galleryPermissionsGranted)
            permissionsLayout.root.show(!state.galleryPermissionsGranted)
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imagesRecyclerView.apply {
                layoutManager = verticalSquareGridLayoutManager(ROW_IMAGES_COUNT)
                adapter = imagesAdapter
                itemAnimator = FadeInDownAnimator()
                addItemDecoration(
                    GridSpaceItemDecoration(
                        ROW_IMAGES_COUNT,
                        getDimension(R.dimen.margin_tiny),
                        true
                    )
                )
            }
            galleryToolbarView.setOnCancelClickListener {
                viewModel.cancelImageSelecting()
            }
            permissionsLayout.apply {
                permissionTextView.setText(R.string.permission_gallery_description)
                allowButton.setOnClickListener {
                    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
                    if (permissionDisabled(permission)) {
                        viewModel.openSettings()
                        return@setOnClickListener
                    }
                    requestPermissionLauncher.launch(permission)
                }
            }
            enterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.TOP).addTarget(galleryToolbarView))
                addTransition(
                    Fade(Fade.IN)
                        .addTarget(permissionsLayout.root)
                        .addTarget(imagesRecyclerView)
                )
            }
            reenterTransition = enterTransition
        }
        bottomBarViewModel.hideBottomBar()
    }
}

