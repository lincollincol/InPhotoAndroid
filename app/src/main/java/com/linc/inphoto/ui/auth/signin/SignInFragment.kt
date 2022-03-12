package com.linc.inphoto.ui.auth.signin

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSignInBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.enable
import com.linc.inphoto.utils.extensions.view.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    // {"email":"xlinc@linc.com","password":"12345678","username":"xlinc"}

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }

    override val viewModel: SignInViewModel by viewModels()
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            signInButton.enable(state.signInEnabled)
            loadingView.show(state.isLoading)
            authErrorTextView.apply {
                text = state.signInErrorMessage.orEmpty()
                show(!state.signInErrorMessage.isNullOrEmpty())
            }
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
                hideKeyboard()
                viewModel.signIn()
            }

            signUpButton.setOnClickListener {
                hideKeyboard()
                viewModel.onSignUp()
            }
        }
    }
}