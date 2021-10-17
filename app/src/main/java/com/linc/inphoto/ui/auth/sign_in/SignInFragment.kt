package com.linc.inphoto.ui.auth.sign_in

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.FragmentSignInBinding
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.base.fragment.BaseStubFragment
import com.linc.inphoto.ui.model.auth.Credentials
import com.linc.inphoto.utils.extensions.textToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseStubFragment<FragmentSignInBinding, SignInViewModel>() {

    // {"email":"xlinc@linc.com","password":"12345678","username":"xlinc"}

    override val viewModel: SignInViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }

    override fun getViewBinding() = FragmentSignInBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener {
            safeResumedLaunch {
                viewModel.signIn(Credentials.SignIn(
                    binding.emailInputField.textToString(),
                    binding.passwordInputField.textToString(),
                ))
            }
        }

        binding.signUpButton.setOnClickListener {
            viewModel.onSignUp()
        }
    }

}