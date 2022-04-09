package com.linc.inphoto.ui.navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.ui.main.MenuTab

class MultiTabNavigator(
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
            val fragment = Navigation.TabScreen(tab)
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
            val fragment = Navigation.TabScreen(tab)
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
        val currentFragment = fragmentManager.fragments
            .firstOrNull { it.isVisible }
            ?: return
        val backstackCount = currentFragment.childFragmentManager.backStackEntryCount
        val currentTabId = (currentFragment as? NavTab)?.containerId
        if (backstackCount > 0 || currentTabId == MenuTab.HOME.name) {
            (currentFragment as? FragmentBackPressedListener)?.onBackPressed()
            return
        }
        if (currentTabId != MenuTab.HOME.name) {
            selectTab(MenuTab.HOME)
            tabSelected?.invoke(MenuTab.HOME)
        }
    }

    override fun applyCommand(command: Command) {
        when (command) {
            is CloseDialog -> closeDialog(command)
            is ShowDialog -> showDialog(command)
            else -> super.applyCommand(command)
        }
    }

    private fun showDialog(command: ShowDialog) {
        val tag = command.screen.screenKey
        val dialog = command.screen.createDialog(fragmentFactory)
        dialog.show(fragmentManager, tag)
    }

    private fun closeDialog(command: CloseDialog) {
        val tag = command.screen.screenKey
        val dialog = fragmentManager.findFragmentByTag(tag)
        if (dialog is DialogFragment)
            dialog.dismiss()
    }
}