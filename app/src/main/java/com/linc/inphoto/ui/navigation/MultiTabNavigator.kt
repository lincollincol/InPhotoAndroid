package com.linc.inphoto.ui.navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.androidx.AppNavigator

class MultiTabNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(activity, containerId, fragmentManager, fragmentFactory) {

    fun selectTab(tab: String) {
        val fragments = fragmentManager.fragments
        val currentFragment = fragments.firstOrNull { it.isVisible }
        val newFragment = fragmentManager.findFragmentByTag(tab)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        if (newFragment == null) {
            transaction.add(
                containerId,
                Navigation.TabSceen(tab).createFragment(fragmentManager.fragmentFactory),
                tab
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
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