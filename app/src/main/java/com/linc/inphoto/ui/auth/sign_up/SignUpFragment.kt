package com.linc.inphoto.ui.auth.sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.FragmentSignUpBinding
import com.linc.inphoto.ui.auth.sign_in.SignInUiState
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.model.auth.Credentials
import com.linc.inphoto.utils.extensions.textToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel, SignUpUiState, BaseUiEffect>() {

    override val viewModel: SignUpViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    override fun getViewBinding() = FragmentSignUpBinding.inflate(layoutInflater)

    override fun handleUiState(state: SignUpUiState) = when(state) {
        else -> null
    }

    override fun handleUiEffect(effect: BaseUiEffect) = when(effect) {
        is BaseUiEffect.Loading -> {}
        is BaseUiEffect.Error -> {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener {
            safeResumedLaunch {
                viewModel.signUp(Credentials.SignUp(
                    binding.emailField.textToString(),
                    binding.usernameField.textToString(),
                    binding.passwordField.textToString(),
                    binding.repeatPasswordField.textToString()
                ))
            }
        }
    }

}