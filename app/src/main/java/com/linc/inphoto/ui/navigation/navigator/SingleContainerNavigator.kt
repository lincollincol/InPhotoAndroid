package com.linc.inphoto.ui.navigation.navigator

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.linc.inphoto.ui.navigation.CloseDialog
import com.linc.inphoto.ui.navigation.CloseTopDialog
import com.linc.inphoto.ui.navigation.ShowDialog

class SingleContainerNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(activity, containerId, fragmentManager, fragmentFactory) {

    override fun applyCommand(command: Command) {
        when (command) {
            is CloseDialog -> closeDialog(command)
            is CloseTopDialog -> closeDialog()
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
        if (dialog is DialogFragment) dialog.dismiss()
    }

    private fun closeDialog() {
        val dialog = fragmentManager.fragments.lastOrNull()
        if (dialog is DialogFragment) dialog.dismiss()
    }

}