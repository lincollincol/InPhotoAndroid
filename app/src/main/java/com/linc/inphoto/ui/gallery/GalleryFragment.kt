package com.linc.inphoto.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentGalleryBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.gallery.item.GalleryImageItem
import com.linc.inphoto.utils.extensions.verticalGridLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery) {

    companion object {
        @JvmStatic
        fun newInstance() = GalleryFragment()
    }

    override val viewModel: GalleryViewModel by viewModels()
    private val binding by viewBinding(FragmentGalleryBinding::bind)
    private val imagesAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() {
        viewModel.uiState.collect { state ->
            imagesAdapter.replaceAll(state.images.map(::GalleryImageItem))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            imagesRecyclerView.apply {
                layoutManager = verticalGridLayoutManager(3)
                adapter = imagesAdapter
            }
        }
        viewModel.loadImages()
    }
}