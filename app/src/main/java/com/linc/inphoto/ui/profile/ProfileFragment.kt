package com.linc.inphoto.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.linc.inphoto.databinding.FragmentProfileBinding
import com.linc.inphoto.ui.base.BaseUiEffect
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.model.auth.Credentials
import com.linc.inphoto.utils.extensions.textToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel, ProfileUiState, BaseUiEffect>() {

    override val viewModel: ProfileViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun handleUiState(state: ProfileUiState) = when(state) {
        is ProfileUiState.UpdateUserData -> {
            binding.usernameTextField.text = state.userModel.name
        }
    }

    override fun handleUiEffect(effect: BaseUiEffect) = when(effect) {
        is BaseUiEffect.Loading -> {}
        is BaseUiEffect.Error -> showErrorMessage(effect.message)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeResumedLaunch {
            viewModel.getUserData()
        }
    }

}