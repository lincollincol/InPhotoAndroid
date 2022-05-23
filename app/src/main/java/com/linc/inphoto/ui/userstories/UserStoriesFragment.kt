package com.linc.inphoto.ui.userstories

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentUserStoriesBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.storiesoverview.StoriesOverviewViewModel
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgumentNotNull
import com.linc.inphoto.utils.extensions.millisToSeconds
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.view.progress.MultiProgressFinishListener
import com.linc.inphoto.utils.view.progress.MultiProgressStepListener
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
            animateTargets(Fade(Fade.IN), root, root.children)
            avatarImageView.loadImage(state.userAvatarUrl)
            nameTextView.text = state.username
            state.stories.getOrNull(state.storyPosition)?.let { story ->
                storyProgressBar.apply {
                    setProgressStepsCount(state.stories.count())
                    setSingleDisplayTime(story.duration.millisToSeconds().toFloat())
                    pause()
                    if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                        start(state.storyPosition)
                    }
                }
                contentImageView.loadImage(story.contentUrl)
            } ?: storyProgressBar.pause()
            state.storyTurn?.let {
                viewModel.storyTurnShown()
                overviewViewModel.selectStoryTurn(it)
            }
            loadingProgressBar.show(state.isLoading)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUserStories(getArgumentNotNull(USER_ID_ARG))
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
            storyProgressBar.apply {
                setListener(MultiProgressStepListener { step ->
                    viewModel.selectStoryStep(step)
                })
                setFinishListener(MultiProgressFinishListener {
                    viewModel.storiesOverviewFinished()
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.storyProgressBar.start()
    }

    override fun onPause() {
        super.onPause()
        binding.storyProgressBar.pause()
    }

}