package com.linc.inphoto.ui.navigation

import com.github.terrakok.cicerone.Router

class AppRouter : Router() {
    fun showDialog(screen: DialogScreen) = executeCommands(ShowDialog(screen))
    fun closeDialog(screen: DialogScreen) = executeCommands(CloseDialog(screen))
    fun closeDialog() = executeCommands(CloseTopDialog)
}