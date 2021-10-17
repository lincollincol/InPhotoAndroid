package com.linc.inphoto.ui.auth.sign_in

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.FragmentLoginBinding
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.model.auth.Credentials
import com.linc.inphoto.utils.extensions.textToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentLoginBinding, SignInViewModel, SignInUiState, BaseUiEffect>() {

    override val viewModel: SignInViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun handleUiState(state: SignInUiState) = when(state) {
        else -> null
    }

    override fun handleUiEffect(effect: BaseUiEffect) = when(effect) {
        is BaseUiEffect.Loading -> {}
        is BaseUiEffect.Error -> {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener {
            safeResumedLaunch {
                viewModel.signIn(Credentials.SignIn(
                    binding.emailField.textToString(),
                    binding.passwordField.textToString(),
                ))
            }
        }

        binding.signUpButton.setOnClickListener {
            viewModel.onSignUp()
        }
    }

}