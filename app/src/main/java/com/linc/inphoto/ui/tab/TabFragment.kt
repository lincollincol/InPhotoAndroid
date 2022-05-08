package com.linc.inphoto.ui.tab

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Replace
import com.linc.inphoto.R
import com.linc.inphoto.ui.main.MenuTab
import com.linc.inphoto.ui.navigation.*
import com.linc.inphoto.ui.navigation.navigator.SingleContainerNavigator
import com.linc.inphoto.utils.extensions.getArgument
import com.linc.inphoto.utils.extensions.safeCast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TabFragment : Fragment(R.layout.fragment_tab), FragmentBackPressedListener, NavContainer {

    companion object {
        private const val TAB_ARG = "container_id"

        @JvmStatic
        fun newInstance(tab: MenuTab) = TabFragment().apply {
            arguments = bundleOf(TAB_ARG to tab)
        }
    }

    private val navigator: Navigator by lazy {
        SingleContainerNavigator(requireActivity(), R.id.tabContainerLayout, childFragmentManager)
    }

    @Inject
    lateinit var navContainerHolder: NavContainerHolder

    override val containerId: String get() = getArgument<MenuTab>(TAB_ARG)?.name.orEmpty()

    override fun onResume() {
        super.onResume()
        navContainerHolder.setNavigator(containerId, navigator)
    }

    override fun onPause() {
        navContainerHolder.removeNavigator(containerId)
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navContainerHolder.initContainer(containerId)
        if (childFragmentManager.findFragmentById(R.id.tabContainerLayout) == null) {
            navigator.applyCommands(arrayOf(Replace(NavScreen.getTabHostScreen(getArgument(TAB_ARG)))))
        }
    }

    override fun onBackPressed() {
        val fragment = childFragmentManager.findFragmentById(R.id.tabContainerLayout)
        fragment?.safeCast<FragmentBackPressedListener>()?.onBackPressed()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        val fragment = childFragmentManager.findFragmentById(R.id.tabContainerLayout)
        fragment?.safeCast<TabStateListener>()?.onTabStateChanged(hidden)
    }

}