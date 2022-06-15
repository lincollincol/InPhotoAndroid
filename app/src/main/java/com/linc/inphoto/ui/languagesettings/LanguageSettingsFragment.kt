package com.linc.inphoto.ui.languagesettings

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import androidx.transition.Slide
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentLanguageSettingsBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.languagesettings.item.LanguageItem
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.collect
import com.linc.inphoto.utils.extensions.createAdapter
import com.linc.inphoto.utils.extensions.view.verticalLinearLayoutManager
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator

@AndroidEntryPoint
class LanguageSettingsFragment : BaseFragment(R.layout.fragment_language_settings) {

    companion object {
        @JvmStatic
        fun newInstance() = LanguageSettingsFragment()
    }

    override val viewModel: LanguageSettingsViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentLanguageSettingsBinding::bind)
    private val languageSection by lazy { Section() }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            languageSection.update(state.languages.map(::LanguageItem))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            languagesRecyclerView.apply {
                layoutManager = verticalLinearLayoutManager()
                adapter = createAdapter(languageSection)
                itemAnimator = FadeInDownAnimator()
            }
            languagesToolbarView.setOnCancelClickListener {
                viewModel.onBackPressed()
            }
            enterTransition = Slide(Gravity.END)
            exitTransition = Fade(Fade.OUT)
            reenterTransition = Fade(Fade.IN)
        }
        viewModel.loadLanguages()
        bottomBarViewModel.hideBottomBar()
    }
}