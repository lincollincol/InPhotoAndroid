package com.linc.inphoto.ui.profilesettings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileSettingsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.view.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileSettingsFragment : BaseFragment(R.layout.fragment_profile_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = ProfileSettingsFragment()
    }

    override val viewModel: ProfileSettingsViewModel by viewModels()
    private val binding by viewBinding(FragmentProfileSettingsBinding::bind)

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            avatarImageView.loadImage(state.imageUri)
            usernameEditText.setText(state.username)
            statusEditText.setText(state.status)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProfileData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            settingsToolbar.setOnDoneClickListener {
                hideKeyboard()
                viewModel.saveProfileData()
            }
            settingsToolbar.setOnCancelClickListener {
                hideKeyboard()
                viewModel.cancelProfileUpdate()
            }
            usernameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateUsername(text.toString())
            }
            statusEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateStatus(text.toString())
            }
        }

    }
}