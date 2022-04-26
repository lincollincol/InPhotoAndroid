package com.linc.inphoto.ui.postcomments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentPostCommentsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.postcomments.item.CommentsPostInfoItem
import com.linc.inphoto.ui.postcomments.item.PostCommentItem
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.systemKeyboard
import com.linc.inphoto.utils.extensions.updateSingle
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber


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
    private val keyboardState by systemKeyboard()
    private val postInfoSection by lazy { Section() }
    private val commentsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            state.postInfoUiState
                ?.let(::CommentsPostInfoItem)
                ?.let(postInfoSection::updateSingle)
            commentsSection.update(state.comments.map(::PostCommentItem))
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

            }
            toolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }

            inputLayout.sendButton.setOnClickListener {
            }
            inputLayout.attachmentsButton.setOnThrottledClickListener {
                Timber.d("Click")
            }
            keyboardState.observeState {
                Timber.d("KEYBOARD $it")
            }
        }
        bottomBarViewModel.hideBottomBar()
    }


}