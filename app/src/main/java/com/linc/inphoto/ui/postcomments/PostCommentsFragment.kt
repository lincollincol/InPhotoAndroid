package com.linc.inphoto.ui.postcomments

import android.os.Bundle
import android.view.View
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
import com.linc.inphoto.utils.extensions.updateSingle
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PostCommentsFragment : BaseFragment(R.layout.fragment_post_comments) {

    companion object {
        @JvmStatic
        fun newInstance() = PostCommentsFragment()
    }

    override val viewModel: PostCommentsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentPostCommentsBinding::bind)
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
        viewModel.loadPostComments()
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
        }
        bottomBarViewModel.hideBottomBar()
    }


}