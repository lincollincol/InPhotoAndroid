package com.linc.inphoto.ui.webpage

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentWebPageBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.getArgument
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebPageFragment : BaseFragment(R.layout.fragment_web_page) {

    companion object {
        private const val TITLE_ARG = "title"
        private const val PAGE_ARG = "page"

        @JvmStatic
        fun newInstance(title: String, pageUrl: String) = WebPageFragment().apply {
            arguments = bundleOf(
                TITLE_ARG to title,
                PAGE_ARG to pageUrl
            )
        }
    }

    override val viewModel: WebPageViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentWebPageBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            state.title?.let(webToolbarView::setToolbarTitle)
            state.pageUrl?.let(webView::loadUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.applyPageParams(getArgument(TITLE_ARG), getArgument(PAGE_ARG))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            webToolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
            enterTransition = Slide(Gravity.END)
            exitTransition = Fade(Fade.OUT)
            reenterTransition = Fade(Fade.IN)
        }
        bottomBarViewModel.hideBottomBar()
    }

}