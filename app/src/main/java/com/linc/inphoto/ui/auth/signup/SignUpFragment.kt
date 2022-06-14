package com.linc.inphoto.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.transition.AutoTransition
import androidx.transition.Fade
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSignUpBinding
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.enable
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
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
            animateTargets(AutoTransition(), contentLayout, contentLayout.children)
            signUpButton.enable(state.signUpEnabled)
            loadingView.show(state.isLoading)
            authErrorTextView.apply {
                text = state.signUpErrorMessage.orEmpty()
                show(!state.signUpErrorMessage.isNullOrEmpty())
            }
            genderRadioGroup.check(
                when (state.gender) {
                    Gender.MALE -> maleRadioButton.id
                    else -> femaleRadioButton.id
                }
            )
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
            signUpButton.setOnThrottledClickListener {
                hideKeyboard()
                viewModel.signUp()
            }
            genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val gender = when (checkedId) {
                    maleRadioButton.id -> Gender.MALE
                    femaleRadioButton.id -> Gender.FEMALE
                    else -> Gender.UNKNOWN
                }
                viewModel.updateGender(gender)
            }
            enterTransition = Fade(Fade.IN)
            reenterTransition = enterTransition
        }
    }

}