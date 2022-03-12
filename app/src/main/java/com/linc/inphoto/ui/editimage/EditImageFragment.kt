package com.linc.inphoto.ui.editimage

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentEditImageBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.editimage.item.EditOperationItem
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.horizontalLinearLayoutManager
import com.linc.inphoto.utils.extensions.view.loadUriImage
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class EditImageFragment : BaseFragment(R.layout.fragment_edit_image) {

    companion object {
        private const val RESULT_KEY_ARG = "result_key"
        private const val IMAGE_URI_ARG = "image_uri"

        @JvmStatic
        fun newInstance(
            resultKey: String,
            image: Uri
        ) = EditImageFragment().apply {
            arguments = bundleOf(
                RESULT_KEY_ARG to resultKey,
                IMAGE_URI_ARG to image
            )
        }
    }

    override val viewModel: EditImageViewModel by viewModels()
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
            }
            editorToolbarView.setOnDoneClickListener {
                viewModel.finishEditing(getArgument(RESULT_KEY_ARG))
            }
            editorToolbarView.setOnCancelClickListener {
                viewModel.cancelEditing()
            }
        }
    }


}