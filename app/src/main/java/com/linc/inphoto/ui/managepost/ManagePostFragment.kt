package com.linc.inphoto.ui.managepost

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentManagePostBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.view.update
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ManagePostFragment : BaseFragment(R.layout.fragment_manage_post) {

    companion object {
        @JvmStatic
        fun newInstance() = ManagePostFragment()
    }

    override val viewModel: ManagePostViewModel by viewModels()
    private val binding by viewBinding(FragmentManagePostBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            tagsEditText.setTags(state.tags)
            descriptionEditText.update(state.description)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            editorToolbarView.apply {
                setOnDoneClickListener(viewModel::savePost)
                setOnCancelClickListener(viewModel::cancelPost)
            }
            tagsEditText.apply {
                setOnTagAddedListener(viewModel::addTags)
                setOnTagDeletedListener(viewModel::removeTags)
            }
            descriptionEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateDescription(text.toString())
            }
        }

    }


}