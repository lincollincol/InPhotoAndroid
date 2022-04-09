package com.linc.inphoto.ui.navigation

import com.github.terrakok.cicerone.Command

data class ShowDialog(val screen: DialogScreen) : Command
data class CloseDialog(val screen: DialogScreen) : Command
object CloseTopDialog : Command
