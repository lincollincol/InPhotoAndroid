package com.linc.inphoto.ui.singlestoryoverview

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.genius.multiprogressbar.MultiProgressBar
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSingleUserStoriesBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.storiesoverview.StoriesOverviewViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.millisToSeconds
import com.linc.inphoto.utils.extensions.view.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserStoriesOverviewFragment : BaseFragment(R.layout.fragment_single_user_stories) {

    companion object {
        private const val USER_ID_ARG = "user_id"

        @JvmStatic
        fun newInstance(userId: String) = UserStoriesOverviewFragment().apply {
            arguments = bundleOf(USER_ID_ARG to userId)
        }
    }

    override val viewModel: UserStoriesOverviewViewModel by viewModels()
    private val storiesOverviewViewModel: StoriesOverviewViewModel by viewModels(
        { parentFragment as Fragment }
    )
    private val binding by viewBinding(FragmentSingleUserStoriesBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            state.userStories?.stories?.let { stories ->
                val story = stories[state.storyPosition]
                progressBar.setProgressStepsCount(stories.count())
                progressBar.setSingleDisplayTime(story.duration.millisToSeconds().toFloat())
                progressBar.pause()
                progressBar.start(state.storyPosition)
                contentImageView.loadImage(story.contentUrl)
            }
            state.storyTurn?.let {
                viewModel.storyTurnShown()
                storiesOverviewViewModel.selectStoryTurn(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            startSideView.setOnClickListener {
                viewModel.previousStoryStep()
            }
            endSideView.setOnClickListener {
                viewModel.nextStoryStep()
            }
            progressBar.apply {
                setListener(object : MultiProgressBar.ProgressStepChangeListener {
                    override fun onProgressStepChange(newStep: Int) {
                        viewModel.selectStoryStep(newStep)
                    }
                })
                setFinishListener(object : MultiProgressBar.ProgressFinishListener {
                    override fun onProgressFinished() {
                        viewModel.storiesOverviewFinished()
                    }
                })
            }
        }
        viewModel.loadUserStories(getArgumentNotNull(USER_ID_ARG))
    }
}