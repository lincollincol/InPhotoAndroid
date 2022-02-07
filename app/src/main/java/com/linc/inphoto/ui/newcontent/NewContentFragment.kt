package com.linc.inphoto.ui.newcontent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.linc.inphoto.R
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NewContentFragment : BaseFragment(R.layout.fragment_new_content) {

    companion object {
        @JvmStatic
        fun newInstance() = NewContentFragment()
    }

    override val viewModel: BaseViewModel<NewContentUiState> by viewModels()

    override suspend fun observeUiState() {
        viewModel.uiState.collect {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}