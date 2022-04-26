package com.linc.inphoto.ui.profilesettings

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentProfileSettingsBinding
import com.linc.inphoto.entity.user.Gender
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.hideKeyboard
import com.linc.inphoto.utils.extensions.systemKeyboard
import com.linc.inphoto.utils.extensions.view.loadImage
import com.linc.inphoto.utils.extensions.view.setError
import com.linc.inphoto.utils.extensions.view.setOnThrottledClickListener
import com.linc.inphoto.utils.extensions.view.update
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileSettingsFragment : BaseFragment(R.layout.fragment_profile_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = ProfileSettingsFragment()
    }

    override val viewModel: ProfileSettingsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentProfileSettingsBinding::bind)
    private val keyboardState by systemKeyboard()

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            avatarImageView.loadImage(state.avatarUri)
            headerImageView.loadImage(state.headerUri)
            usernameEditText.update(state.username)
            statusEditText.update(state.status)
            usernameTextLayout.setError(
                !state.isValidUsername,
                R.string.settings_invalid_username_error
            )
            genderRadioGroup.check(
                when (state.gender) {
                    Gender.MALE -> maleRadioButton.id
                    else -> femaleRadioButton.id
                }
            )
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
            uploadAvatarButton.setOnThrottledClickListener {
                viewModel.updateAvatar()
                view.clearFocus()
            }
            uploadHeaderButton.setOnThrottledClickListener {
                viewModel.updateHeader()
                view.clearFocus()
            }
            randomAvatarButton.setOnThrottledClickListener {
                viewModel.randomAvatar()
                view.clearFocus()
            }
            randomHeaderButton.setOnThrottledClickListener {
                viewModel.randomHeader()
                view.clearFocus()
            }
            usernameEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateUsername(text.toString())
            }
            statusEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.updateStatus(text.toString())
            }
            genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                val gender = when (checkedId) {
                    maleRadioButton.id -> Gender.MALE
                    femaleRadioButton.id -> Gender.FEMALE
                    else -> Gender.UNKNOWN
                }
                viewModel.updateGender(gender)
            }
            keyboardState.observeState {
                bottomBarViewModel.showBottomBar(!it)
            }
        }
        bottomBarViewModel.showBottomBar()
    }
}