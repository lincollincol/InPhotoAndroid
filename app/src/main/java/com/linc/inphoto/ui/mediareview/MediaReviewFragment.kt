package com.linc.inphoto.ui.mediareview

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
import com.linc.inphoto.databinding.FragmentMediaReviewBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.mediareview.item.MediaReviewItem
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.reduceDragSensitivity
import com.linc.inphoto.utils.extensions.view.setSafeOffscreenPageLimit
import com.linc.inphoto.utils.view.viewpager.DepthPageTransformer
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
    private var mediaPageAdapter: MediaPageAdapter? = null

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            mediaPageAdapter?.apply {
                setPages(state.files)
//                storiesViewPager.setSafeOffscreenPageLimit(state.stories.count())
            }
            filesSection.update(state.files.map(::MediaReviewItem))
            filesViewPager.setSafeOffscreenPageLimit(state.files.count())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPageAdapter = MediaPageAdapter(this)
        viewModel.applyMediaFiles(getArgument(MEDIA_FILES_ARG) ?: emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            filesViewPager.apply {
                adapter = mediaPageAdapter
                setPageTransformer(DepthPageTransformer())
                reduceDragSensitivity()
//                adapter = createAdapter(filesSection)
//                setPageTransformer(DepthPageTransformer())
            }
            reviewToolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
            enterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.TOP)).addTarget(reviewToolbarView)
                addTransition(Fade(Fade.IN).addTarget(filesViewPager))
            }
            reenterTransition = enterTransition
            exitTransition = Fade(Fade.OUT)
        }
        bottomBarViewModel.hideBottomBar()
    }
}