package com.linc.inphoto.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSplashBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }

    override val viewModel: SplashViewModel by viewModels()

    private val binding by viewBinding(FragmentSplashBinding::bind)

    override suspend fun observeUiState() {
        // Empty UI state
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeResumedLaunch {
            viewModel.checkLoggedIn()
        }
    }

}