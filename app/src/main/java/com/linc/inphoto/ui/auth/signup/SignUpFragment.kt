package com.linc.inphoto.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.FragmentSignUpBinding
import com.linc.inphoto.ui.auth.model.Credentials
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.enable
import com.linc.inphoto.utils.extensions.textToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    override val viewModel: SignUpViewModel by viewModels()

    override val binding: FragmentSignUpBinding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            signUpButton.enable(state.signUpEnabled)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpButton.setOnClickListener {
            safeResumedLaunch {
                viewModel.signUp(
                    Credentials.SignUp(
                        binding.emailInputField.textToString(),
                        binding.usernameInputField.textToString(),
                        binding.passwordInputField.textToString(),
                    binding.repeatPasswordInputField.textToString()
                ))
            }
        }
    }

}