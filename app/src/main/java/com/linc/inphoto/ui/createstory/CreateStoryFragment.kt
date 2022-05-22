package com.linc.inphoto.ui.createstory

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentCreateStoryBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.DateFormatter
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyStoryContent(getArgument(CONTENT_URI_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            durationSettingsTextView.setOnThrottledClickListener {
                viewModel.selectDurationTime()
            }
            expirationSettingsTextView.setOnThrottledClickListener {
                viewModel.selectExpirationTime()
            }
        }
    }

}