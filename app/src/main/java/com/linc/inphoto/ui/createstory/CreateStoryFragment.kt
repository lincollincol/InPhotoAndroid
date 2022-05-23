package com.linc.inphoto.ui.createstory

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentCreateStoryBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStoryFragment : BaseFragment(R.layout.fragment_create_story) {

    companion object {
        private const val CONTENT_URI_ARG = "content_uri"

        @JvmStatic
        fun newInstance(contentUri: Uri) = CreateStoryFragment().apply {
            arguments = bundleOf(CONTENT_URI_ARG to contentUri)
        }
    }

    override val viewModel: CreateStoryViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentCreateStoryBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            animateTargets(Fade(Fade.IN), contentLayout, contentLayout.children)
            storyImageView.loadImage(state.contentUri)
            durationSettingsTextView.setValue(
                DateFormatter.getDuration(requireContext(), state.durationMillis)
            )
            expirationSettingsTextView.setValue(
                DateFormatter.getDuration(requireContext(), state.expirationTimeMillis)
            )
            loadingView.show(state.isLoading)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyStoryContent(getArgument(CONTENT_URI_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbarView.apply {
                setOnDoneClickListener { viewModel.saveStory() }
                setOnCancelClickListener { viewModel.onBackPressed() }
            }
            durationSettingsTextView.setOnThrottledClickListener {
                viewModel.selectDurationTime()
            }
            expirationSettingsTextView.setOnThrottledClickListener {
                viewModel.selectExpirationTime()
            }
            enterTransition = TransitionSet().apply {
                addTransition(Fade(Fade.IN).addTarget(contentLayout))
            }
            reenterTransition = enterTransition
        }
        bottomBarViewModel.hideBottomBar()
    }

}