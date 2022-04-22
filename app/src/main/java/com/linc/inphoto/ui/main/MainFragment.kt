package com.linc.inphoto.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.Slide
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentMainBinding
import com.linc.inphoto.di.navigation.LocalNavigatorHolder
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.navigator.MultiContainerNavigator
import com.linc.inphoto.utils.extensions.animateTargets
import com.linc.inphoto.utils.extensions.findVisibleFragment
import com.linc.inphoto.utils.extensions.safeCast
import com.linc.inphoto.utils.extensions.view.show
import com.linc.inphoto.utils.keyboard.KeyboardStateListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main), FragmentBackPressedListener,
    KeyboardStateListener {

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    @LocalNavigatorHolder
    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    override val viewModel: MainViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val navigator by lazy {
        MultiContainerNavigator(requireActivity(), R.id.mainContainerLayout, childFragmentManager)
    }

    override suspend fun observeUiState() = with(binding) {
        bottomBarViewModel.uiState.collect { state ->
            animateTargets(Slide(Gravity.BOTTOM).apply {
                setDuration(800); setInterpolator(
                AccelerateDecelerateInterpolator()
            )
            }, mainLayout, bottomNavigationView)
            bottomNavigationView.show(state.visible)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigator.initNavigator(MenuTab.HOME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView.onTabSelected = { tab ->
            MenuTab.fromId(tab.id)?.let(navigator::selectTab)
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        navigator.handleBackPressed { tab ->
            binding.bottomNavigationView.selectTabById(tab.id)
        }
    }

    override fun onKeyboardStateChanged(visible: Boolean) {
        childFragmentManager.findVisibleFragment()
            ?.safeCast<KeyboardStateListener>()
            ?.onKeyboardStateChanged(visible)
    }
}