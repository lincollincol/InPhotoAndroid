package com.linc.inphoto.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSignUpBinding
import com.linc.inphoto.ui.auth.model.Credentials
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.textToString
import com.linc.inphoto.utils.extensions.view.enable
import com.linc.inphoto.utils.extensions.view.show
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
            loadingView.show(state.isLoading)
            authErrorTextView.apply {
                text = state.signUpErrorMessage.orEmpty()
                show(!state.signUpErrorMessage.isNullOrEmpty())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            emailEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateEmail(text.toString())
            }
            usernameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateUsername(text.toString())
            }
            passwordEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updatePassword(text.toString())
            }
            repeatPasswordEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateRepeatPassword(text.toString())
            }
            signUpButton.setOnClickListener {
                viewModel.signUp(
                    Credentials.SignUp(
                        emailEditText.textToString(),
                        usernameEditText.textToString(),
                        passwordEditText.textToString(),
                        repeatPasswordEditText.textToString()
                    )
                )
            }
        }
    }

}