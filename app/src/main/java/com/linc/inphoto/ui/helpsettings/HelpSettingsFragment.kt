package com.linc.inphoto.ui.helpsettings

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentHelpSettingsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.helpsettings.item.HelpSettingsOptionItem
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class HelpSettingsFragment : BaseFragment(R.layout.fragment_help_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = HelpSettingsFragment()
    }

    override val viewModel: HelpSettingsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentHelpSettingsBinding::bind)
    private val helpOptionsSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            helpOptionsSection.update(state.settingsOptions.map(::HelpSettingsOptionItem))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            optionsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(helpOptionsSection)
                itemAnimator = FadeInDownAnimator()
            }
            helpToolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
            enterTransition = Slide(Gravity.END)
            exitTransition = Fade(Fade.OUT)
            reenterTransition = Fade(Fade.IN)
        }
        viewModel.loadHelpOptions()
        bottomBarViewModel.hideBottomBar()
    }
}