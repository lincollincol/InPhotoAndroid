package com.linc.inphoto.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSplashBinding
import com.linc.inphoto.ui.base.fragment.BaseStubFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseStubFragment<FragmentSplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = SplashFragment()
    }

    override fun getViewBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModel.checkLoggedIn()
        }
    }

}