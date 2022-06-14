package com.linc.inphoto.ui.postcomments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentPostCommentsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.postcomments.item.CommentsPostInfoItem
import com.linc.inphoto.ui.postcomments.item.PostCommentItem
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.updateSingle
import com.linc.inphoto.utils.extensions.view.*
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PostCommentsFragment : BaseFragment(R.layout.fragment_post_comments) {

    companion object {
        private const val POST_ID_ARG = "post_id"

        @JvmStatic
        fun newInstance(postId: String) = PostCommentsFragment().apply {
            arguments = bundleOf(POST_ID_ARG to postId)
        }
    }

    override val viewModel: PostCommentsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentPostCommentsBinding::bind)
    private val postInfoSection by lazy { Section() }
    private val commentsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            inputLayout.apply {
                animateTargets(Fade(), inputLayout.root, inputLayout.root.children)
                sendButton.enable(state.isCommentValid)
                doneButton.enable(state.isCommentValid)
                sendButton.show(!state.isEditorState)
                doneButton.show(state.isEditorState)
                attachmentsButton.show(!state.isEditorState)
                cancelButton.show(state.isEditorState)
                inputEditText.update(state.commentMessage)
            }
            state.postInfoUiState
                ?.let(::CommentsPostInfoItem)
                ?.let(postInfoSection::updateSingle)
            commentsSection.update(state.comments.reversed().map(::PostCommentItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadPostComments(getArgument(POST_ID_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            commentsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(hasStableIds = true, postInfoSection, commentsSection)
                itemAnimator = FadeInDownAnimator()
            }
            toolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
            inputLayout.apply {
                sendButton.setOnThrottledClickListener {
                    viewModel.saveComment()
                    commentsRecyclerView.smoothScrollToEnd()
                }
                doneButton.setOnThrottledClickListener {
                    viewModel.updateComment()
                }
                inputEditText.doOnTextChanged { text, _, _, _ ->
                    viewModel.updateCommentMessage(text.toString())
                }
                cancelButton.setOnThrottledClickListener {
                    viewModel.cancelCommentEditor()
                }
                attachmentsButton.setOnThrottledClickListener {
                    Snackbar.make(requireContext(), view, "Soon", Snackbar.LENGTH_SHORT).show()
                }
            }
            enterTransition = TransitionSet().apply {
                addTransition(Slide(Gravity.TOP).addTarget(toolbarView))
                addTransition(Slide(Gravity.BOTTOM).addTarget(inputLayout.root))
            }
            reenterTransition = enterTransition
        }
        bottomBarViewModel.hideBottomBar()
    }


}