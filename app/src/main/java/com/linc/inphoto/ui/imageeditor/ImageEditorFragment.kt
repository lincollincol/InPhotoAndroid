package com.linc.inphoto.ui.imageeditor

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentImageEditorBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.imageeditor.item.CropRatioItem
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.horizontalLinearLayoutManager
import com.linc.inphoto.utils.extensions.setAspectRatio
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ImageEditorFragment : BaseFragment(R.layout.fragment_image_editor) {

    companion object {
        private const val IMAGE_URI_ARG = "uri_arg"

        @JvmStatic
        fun newInstance(image: Uri) = ImageEditorFragment().apply {
            arguments = bundleOf(IMAGE_URI_ARG to image)
        }
    }

    override val viewModel: ImageEditorViewModel by viewModels()
    private val binding by viewBinding(FragmentImageEditorBinding::bind)
    private val ratioAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            ratioAdapter.update(state.ratioItems.map(::CropRatioItem))
            cropImageLayout.cropView.configureOverlay()
                .setAspectRatio(state.currentRatio)
                .apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            cropImageLayout.ratioRecyclerView.apply {
                layoutManager = horizontalLinearLayoutManager()
                adapter = ratioAdapter
            }
            cropImageLayout.cropView.apply {
                setImageUri(getArgument(IMAGE_URI_ARG))
            }
        }
        viewModel.loadAvailableRatios()
    }

}