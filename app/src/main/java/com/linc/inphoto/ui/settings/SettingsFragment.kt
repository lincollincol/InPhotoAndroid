package com.linc.inphoto.ui.settings

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentSettingsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.ui.settings.item.SettingsOptionItem
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override val viewModel: SettingsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val optionsAdapter: GroupieAdapter by lazy { GroupieAdapter() }

    override suspend fun observeUiState() {
        viewModel.uiState.collect { state ->
            optionsAdapter.update(state.settingsOptions.map(::SettingsOptionItem))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSettings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            optionsRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = optionsAdapter
                itemAnimator = FadeInDownAnimator()
            }
            settingsToolbar.setOnCancelClickListener {
                viewModel.closeSettings()
            }
            enterTransition = Slide(Gravity.END)
            exitTransition = Fade(Fade.OUT)
            reenterTransition = Fade(Fade.IN)
        }
        bottomBarViewModel.hideBottomBar()
    }
}