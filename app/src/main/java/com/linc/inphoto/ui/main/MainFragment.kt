package com.linc.inphoto.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentMainBinding
import com.linc.inphoto.di.navigation.LocalNavigatorHolder
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.navigator.MultiContainerNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), FragmentBackPressedListener {

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val navigator by lazy {
        MultiContainerNavigator(requireActivity(), R.id.mainContainerLayout, childFragmentManager)
    }

    @LocalNavigatorHolder
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigator.initNavigator(MenuTab.PROFILE)
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
}