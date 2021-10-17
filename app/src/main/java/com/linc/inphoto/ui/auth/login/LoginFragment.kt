package com.linc.inphoto.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentLoginBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel, LoginUiState, LoginUiEffect>() {

    override val viewModel: LoginViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun handleUiState(state: LoginUiState) = when(state) {
        else -> null
    }

    override fun handleUiEffect(effect: LoginUiEffect) = when(effect) {
        else -> null
    }

}