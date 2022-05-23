package com.linc.inphoto.ui.editimage

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
import com.linc.inphoto.databinding.FragmentEditImageBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.editimage.item.EditOperationItem
import com.linc.inphoto.ui.editimage.model.EditorIntent
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.horizontalLinearLayoutManager
import com.linc.inphoto.utils.extensions.view.loadUriImage
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class EditImageFragment : BaseFragment(R.layout.fragment_edit_image) {

    companion object {
        private const val INTENT_ARG = "intent_key"
        private const val IMAGE_URI_ARG = "image_uri"

        @JvmStatic
        fun newInstance(
            intent: EditorIntent,
            image: Uri
        ) = EditImageFragment().apply {
            arguments = bundleOf(
                INTENT_ARG to intent,
                IMAGE_URI_ARG to image
            )
        }
    }

    override val viewModel: EditImageViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentEditImageBinding::bind)
    private val editorActionsAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            previewImageView.loadUriImage(state.imageUri)
            editorActionsAdapter.update(state.editOperations.map(::EditOperationItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyImage(getArgument(IMAGE_URI_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            operationsRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = editorActionsAdapter
                itemAnimator = FadeInLeftAnimator()
            }
            editorToolbarView.setOnDoneClickListener {
                viewModel.finishEditing(getArgument(INTENT_ARG))
            }
            editorToolbarView.setOnCancelClickListener {
                viewModel.cancelEditing()
            }
            enterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.TOP).addTarget(editorToolbarView))
                addTransition(Slide(Gravity.BOTTOM).addTarget(operationsRecyclerView))
                addTransition(Fade(Fade.IN).addTarget(previewImageView))
            }
            reenterTransition = enterTransition
        }
        bottomBarViewModel.hideBottomBar()
    }


}