package com.linc.inphoto.ui.navigation.navigator

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.ui.main.MenuTab
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.NavContainer
import com.linc.inphoto.ui.navigation.NavScreen
import com.linc.inphoto.utils.extensions.findVisibleFragment
import com.linc.inphoto.utils.extensions.safeCast

class MultiContainerNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(activity, containerId, fragmentManager, fragmentFactory) {

    protected var defaultHostTab: MenuTab? = null

    fun initNavigator(defaultHostTab: MenuTab) {
        this.defaultHostTab = defaultHostTab
        MenuTab.values().forEach { tab ->
            val transaction = fragmentManager.beginTransaction()
            val fragment = NavScreen.TabScreen(tab)
                .createFragment(fragmentManager.fragmentFactory)
            transaction.add(containerId, fragment, tab.name)
            if (tab != defaultHostTab) {
                transaction.hide(fragment)
            }
            transaction.commitNow()
        }
    }

    fun selectTab(tab: MenuTab) {
        val fragments = fragmentManager.fragments
        val currentFragment = fragments.firstOrNull { it.isVisible }
        val newFragment = fragmentManager.findFragmentByTag(tab.name)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        if (newFragment == null) {
            val fragment = NavScreen.TabScreen(tab)
                .createFragment(fragmentManager.fragmentFactory)
            transaction.add(containerId, fragment, tab.name)
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    fun handleBackPressed(tabSelected: ((MenuTab) -> Unit)? = null) {
        val currentFragment = fragmentManager.findVisibleFragment() ?: return
        val backstackCount = currentFragment.childFragmentManager.backStackEntryCount
        val currentTabId = (currentFragment as? NavContainer)?.containerId
        if (backstackCount > 0 || currentTabId == MenuTab.HOME.name) {
            currentFragment.safeCast<FragmentBackPressedListener>()?.onBackPressed()
            return
        }
        if (currentTabId != MenuTab.HOME.name) {
            selectTab(MenuTab.HOME)
            tabSelected?.invoke(MenuTab.HOME)
        }
    }

}