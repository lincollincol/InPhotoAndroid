package com.linc.inphoto.ui.managepost

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentManagePostBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.managepost.model.ManageablePost
import com.linc.inphoto.utils.extensions.autoAnimateTargets
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.scrollToBottom
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.extensions.view.update
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ManagePostFragment : BaseFragment(R.layout.fragment_manage_post) {

    companion object {
        private const val POST_ARG = "post_uri"

        @JvmStatic
        fun newInstance(post: ManageablePost) = ManagePostFragment().apply {
            arguments = bundleOf(POST_ARG to post)
        }
    }

    override val viewModel: ManagePostViewModel by viewModels()
    private val binding by viewBinding(FragmentManagePostBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            postImageView.loadImage(state.imageUri, reloadImage = false)
            tagsEditText.setTags(state.tags)
            descriptionEditText.update(state.description)
//            descriptionEditText.setText(state.description)
            loadingView.show(state.isLoading)
            autoAnimateTargets(root, descriptionErrorTextView, tagsErrorTextView)
            descriptionErrorTextView.show(!state.isValidDescription && state.isErrorsEnabled)
            tagsErrorTextView.show(!state.isValidTags && state.isErrorsEnabled)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgument<ManageablePost>(POST_ARG)?.let(viewModel::applyPost)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            editorToolbarView.apply {
                setOnDoneClickListener {
                    hideKeyboard()
                    view.clearFocus()
                    viewModel.savePost()
                }
                setOnCancelClickListener {
                    hideKeyboard()
                    viewModel.cancelPost()
                }
            }
            tagsEditText.apply {
                setOnTagAddedListener {
                    viewModel.addTags(it)
                    contentScrollView.scrollToBottom(contentLayout)
                }
                setOnTagDeletedListener(viewModel::removeTags)
            }
            descriptionEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateDescription(text.toString())
            }
        }

    }


}