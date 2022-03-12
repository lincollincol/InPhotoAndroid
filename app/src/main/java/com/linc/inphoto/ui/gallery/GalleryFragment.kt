package com.linc.inphoto.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentGalleryBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.gallery.item.GalleryImageItem
import com.linc.inphoto.utils.GridSpaceItemDecoration
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.getDimension
import com.linc.inphoto.utils.extensions.verticalGridLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery) {

    companion object {
        private const val ROW_IMAGES_COUNT = 3
        private const val RESULT_KEY_ARG = "result_key"

        @JvmStatic
        fun newInstance(resultKey: String) = GalleryFragment().apply {
            arguments = bundleOf(RESULT_KEY_ARG to resultKey)
        }
    }

    override val viewModel: GalleryViewModel by viewModels()
    private val binding by viewBinding(FragmentGalleryBinding::bind)
    private val imagesAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() {
        viewModel.uiState.collect { state ->
            imagesAdapter.replaceAll(state.images.map(::GalleryImageItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadImages(getArgument(RESULT_KEY_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imagesRecyclerView.apply {
                layoutManager = verticalGridLayoutManager(ROW_IMAGES_COUNT)
                adapter = imagesAdapter
                addItemDecoration(
                    GridSpaceItemDecoration(
                        ROW_IMAGES_COUNT,
                        getDimension(R.dimen.margin_small),
                        true
                    )
                )
            }
            galleryToolbar.setOnCancelClickListener {
                viewModel.cancelImageSelecting()
            }
        }
    }
}