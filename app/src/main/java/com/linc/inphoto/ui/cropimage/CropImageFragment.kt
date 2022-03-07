package com.linc.inphoto.ui.cropimage

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentCropImageBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.cropimage.item.CropRatioItem
import com.linc.inphoto.ui.cropimage.model.CropShape
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.show
import com.steelkiwi.cropiwa.AspectRatio
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CropImageFragment : BaseFragment(R.layout.fragment_crop_image) {

    companion object {
        private const val RESULT_KEY_ARG = "result_key"
        private const val IMAGE_URI_ARG = "image_uri"

        @JvmStatic
        fun newInstance(
            resultKey: String,
            image: Uri
        ) = CropImageFragment().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
                IMAGE_URI_ARG to image
            )
        }
    }

    override val viewModel: CropImageViewModel by viewModels()
    private val binding by viewBinding(FragmentCropImageBinding::bind)
    private val ratioAdapter by lazy { GroupieAdapter() }
    private val editorActionsAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            cropView.apply {
                setImageUri(getArgument(IMAGE_URI_ARG))
                configureOverlay()
                    .setAspectRatio(AspectRatio.IMG_SRC)
                    .apply()

            }
            autoAnimateTargets(
                root,
                ratioRecyclerView,
                shapeSettingsTextView,
                dynamicOverlaySwitch
            )
            ratioAdapter.update(state.ratioItems.map(::CropRatioItem))
            cropView.configureOverlay()
                .setDynamicCrop(state.isDynamicOverlay)
                .setAspectRatio(state.currentRatio)
                .toggleShape(state.cropShape is CropShape.Rect)
                .apply()
            ratioRecyclerView.show(!state.isDynamicOverlay)
            shapeSettingsTextView.setValue(state.cropShape.value)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ratioRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = ratioAdapter
            }
            cropView.apply {
                setImageUri(getArgument(IMAGE_URI_ARG))
                configureOverlay()
                    .setAspectRatio(AspectRatio.IMG_SRC)
                    .apply()
            }
            shapeSettingsTextView.setOnClickListener {
                viewModel.selectCropShape()
            }
            dynamicOverlaySwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.changeOverlayType(checked)
            }
        }
        viewModel.loadAvailableRatios()
    }
}