package com.linc.inphoto.ui.navigation

import com.github.terrakok.cicerone.Router

class AppRouter : Router() {
    fun showDialog(screen: DialogScreen) = executeCommands(ShowDialog(screen))
}