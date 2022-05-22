package com.linc.inphoto.ui.createstory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentCreateStoryBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.collect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStoryFragment : BaseFragment(R.layout.fragment_create_story) {

    override val viewModel: CreateStoryViewModel by viewModels()
    private val binding by viewBinding(FragmentCreateStoryBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}