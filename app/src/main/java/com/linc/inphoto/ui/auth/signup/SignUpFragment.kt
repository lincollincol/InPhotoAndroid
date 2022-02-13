package com.linc.inphoto.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSignUpBinding
import com.linc.inphoto.ui.auth.model.Credentials
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.textToString
import com.linc.inphoto.utils.extensions.view.enable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }

    override val viewModel: SignUpViewModel by viewModels()
    private val binding by viewBinding(FragmentSignUpBinding::bind)

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