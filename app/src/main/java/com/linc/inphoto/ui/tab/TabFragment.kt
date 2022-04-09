package com.linc.inphoto.ui.tab

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.R
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.NavContainerHolder
import com.linc.inphoto.ui.navigation.NavTab
import com.linc.inphoto.ui.navigation.Navigation
import com.linc.inphoto.utils.extensions.getArgument
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TabFragment : Fragment(R.layout.fragment_tab), FragmentBackPressedListener, NavTab {

    companion object {
        private const val CONTAINER_ID_ARG = "container_id"

        @JvmStatic
        fun newInstance(id: String) = TabFragment().apply {
            arguments = bundleOf(CONTAINER_ID_ARG to id)
        }
    }

    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.tabContainerLayout, childFragmentManager)
    }

    @Inject
    lateinit var navContainerHolder: NavContainerHolder

    override val containerId: String get() = getArgument<String>(CONTAINER_ID_ARG).orEmpty()

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
//        viewModel.setupNavContainer(requireArguments().getString(CONTAINER_ID_ARG))
        navContainerHolder.initContainer(containerId)
        if (childFragmentManager.findFragmentById(R.id.tabContainerLayout) == null) {
            navigator.applyCommands(arrayOf(Replace(Navigation.getTabHostScreen(containerId))))
        }
    }

    override fun onBackPressed() {
        childFragmentManager.findFragmentById(R.id.tabContainerLayout)?.let {
            (it as? FragmentBackPressedListener)?.onBackPressed()
        } /*?: super.onBackPressed()*/
    }
}