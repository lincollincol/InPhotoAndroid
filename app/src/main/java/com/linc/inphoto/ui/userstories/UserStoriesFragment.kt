package com.linc.inphoto.ui.userstories

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.genius.multiprogressbar.MultiProgressBar
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentUserStoriesBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.storiesoverview.StoriesOverviewViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.millisToSeconds
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserStoriesFragment : BaseFragment(R.layout.fragment_user_stories) {

    companion object {
        private const val USER_ID_ARG = "user_id"

        @JvmStatic
        fun newInstance(userId: String) = UserStoriesFragment().apply {
            arguments = bundleOf(USER_ID_ARG to userId)
        }
    }

    override val viewModel: UserStoriesViewModel by viewModels()
    private val overviewViewModel: StoriesOverviewViewModel by viewModels(::requireParentFragment)
    private val binding by viewBinding(FragmentUserStoriesBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            avatarImageView.loadImage(state.userAvatarUrl)
            nameTextView.text = state.username
            state.stories.getOrNull(state.storyPosition)?.let { story ->
                progressBar.apply {
                    setProgressStepsCount(state.stories.count())
                    setSingleDisplayTime(story.duration.millisToSeconds().toFloat())
                    pause()
                    start(state.storyPosition)
                }
                contentImageView.loadImage(story.contentUrl)
            }
            state.storyTurn?.let {
                viewModel.storyTurnShown()
                overviewViewModel.selectStoryTurn(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            avatarImageView.setOnThrottledClickListener {
                viewModel.selectProfile()
            }
            nameTextView.setOnThrottledClickListener {
                viewModel.selectProfile()
            }
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