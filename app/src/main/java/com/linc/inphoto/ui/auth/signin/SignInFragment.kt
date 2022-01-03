package com.linc.inphoto.ui.auth.signin

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.FragmentSignInBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.enable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding, SignInViewModel>() {

    // {"email":"xlinc@linc.com","password":"12345678","username":"xlinc"}

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }

    override val viewModel: SignInViewModel by viewModels()
    override val binding: FragmentSignInBinding by lazy {
        FragmentSignInBinding.inflate(layoutInflater)
    }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            signInButton.enable(state.signInEnabled)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateLogin(text.toString())
            }

            passwordEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updatePassword(text.toString())
            }

            signInButton.setOnClickListener {
                viewModel.signIn()
            }

            signUpButton.setOnClickListener {
                viewModel.onSignUp()
            }
        }
    }
}