package com.linc.inphoto.ui.tagposts

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentTagPostsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.tagposts.item.TagPostItem
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.verticalSquareGridLayoutManager
import com.linc.inphoto.utils.view.VerticalNestedScrollListener
import com.linc.inphoto.utils.view.recyclerview.decorator.GridSpaceItemDecoration
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class TagPostsFragment : BaseFragment(R.layout.fragment_tag_posts) {

    companion object {
        private const val TAG_ID_ARG = "tag_id"
        private const val ROW_POSTS_COUNT = 3

        @JvmStatic
        fun newInstance(tagId: String) = TagPostsFragment().apply {
            arguments = bundleOf(TAG_ID_ARG to tagId)
        }
    }

    override val viewModel: TagPostsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentTagPostsBinding::bind)
    private val postInfoSection by lazy { Section() }
    private val postsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            animateTargets(Fade(Fade.IN), tagPostsLayout, tagPostsLayout.children)
            previewImageView.apply {
                loadImage(state.postPreviewUrl)
                show(state.isTagInfoValid)
            }
            state.name?.let {
                tagPostsToolbar.setToolbarTitle(context?.getString(R.string.tag_title, it))
                tagTextView.apply {
                    text = context?.getString(R.string.tag_title, it)
                    show(state.isTagInfoValid)
                }
            }
            postsCountTextView.apply {
                text = context?.getString(
                    R.string.posts_count,
                    state.postsCount.compactNumber()
                )
                show(state.isTagInfoValid)
            }
            postsSection.update(state.posts.map(::TagPostItem))
            tagPostsProgressBar.show(state.isLoading)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            postsRecyclerView.apply {
                layoutManager = verticalSquareGridLayoutManager(ROW_POSTS_COUNT)
                adapter = createAdapter(postInfoSection, postsSection)
                itemAnimator = FadeInDownAnimator()
                addItemDecoration(
                    GridSpaceItemDecoration(
                        ROW_POSTS_COUNT,
                        getDimension(R.dimen.margin_tiny),
                        true
                    )
                )
            }
            tagPostsToolbar.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
            tagPostsScrollView.setOnScrollChangeListener(VerticalNestedScrollListener {
                when (it) {
                    Gravity.BOTTOM -> bottomBarViewModel.hideBottomBar()
                    Gravity.TOP -> bottomBarViewModel.showBottomBar()
                }
            })
        }
        viewModel.loadTagPosts(getArgument(TAG_ID_ARG))
        bottomBarViewModel.showBottomBar()
    }

}