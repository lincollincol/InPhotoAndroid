package com.linc.inphoto.ui.cropimage

import android.net.Uri
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
import com.linc.inphoto.databinding.FragmentCropImageBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.cropimage.item.CropRatioItem
import com.linc.inphoto.ui.cropimage.model.CropIntent
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.autoAnimateTargets
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CropImageFragment : BaseFragment(R.layout.fragment_crop_image) {

    companion object {
        private const val INTENT_ARG = "intent_key"
        private const val IMAGE_URI_ARG = "image_uri"

        @JvmStatic
        fun newInstance(
            intent: CropIntent,
            image: Uri
        ) = CropImageFragment().apply {
            arguments = bundleOf(
                INTENT_ARG to intent,
                IMAGE_URI_ARG to image
            )
        }
    }

    override val viewModel: CropImageViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentCropImageBinding::bind)
    private val ratioAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            autoAnimateTargets(
                root,
                ratioRecyclerView,
                shapeSettingsTextView,
                fixedRatioSwitch
            )
            ratioAdapter.update(state.ratioItems.map(::CropRatioItem))
            cropView.apply {
                setFixedAspectRatio(state.isFixedAspectRatio)
                setAspectRatio(state.currentRatio)
                setCropShape(state.cropShape)
            }

            ratioRecyclerView.show(state.isFixedAspectRatio)
            fixedRatioSwitch.isChecked = state.isFixedAspectRatio
            shapeSettingsTextView.setValue(state.cropShape.value)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ratioRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = ratioAdapter
                itemAnimator = FadeInLeftAnimator()
            }
            cropView.apply {
                setImageUriAsync(getArgument(IMAGE_URI_ARG))
                setOnCropImageCompleteListener { view, result ->
                    result.applyShape(view)
                    viewModel.saveCroppedImage(result.uriContent)
                }
            }
            editorToolbarView.setOnDoneClickListener {
                cropView.croppedImageAsync()
            }
            editorToolbarView.setOnCancelClickListener {
                viewModel.cancelCropping()
            }
            shapeSettingsTextView.setOnClickListener {
                viewModel.selectCropShape()
            }
            fixedRatioSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.changeRatioState(checked)
            }
            enterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.BOTTOM).addTarget(cropControllersLayout))
                addTransition(Fade(Fade.IN).addTarget(cropView))
            }
            reenterTransition = enterTransition
        }
        bottomBarViewModel.hideBottomBar()
        viewModel.run {
            specifyIntent(getArgument(INTENT_ARG))
            prepareCrop()
        }
    }
}