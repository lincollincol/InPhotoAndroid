package com.linc.inphoto.ui.mediareview

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentMediaReviewBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.mediareview.item.MediaReviewItem
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.reduceDragSensitivity
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaReviewFragment : BaseFragment(R.layout.fragment_media_review) {

    companion object {
        private const val MEDIA_FILES_ARG = "media_files"

        @JvmStatic
        fun newInstance(
            files: List<Uri>
        ) = MediaReviewFragment().apply {
            arguments = bundleOf(MEDIA_FILES_ARG to files)
        }
    }

    override val viewModel: MediaReviewViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentMediaReviewBinding::bind)
    private val filesSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            filesSection.update(state.files.map(::MediaReviewItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyMediaFiles(getArgument(MEDIA_FILES_ARG) ?: emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            filesRecyclerView.apply {
                adapter = createAdapter(filesSection)
                reduceDragSensitivity()
            }
            reviewToolbar.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
        }
        bottomBarViewModel.hideBottomBar()
    }
}