package com.linc.inphoto.ui.main.model

import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.main.MenuTab

data class BottomBarUiState(
    val visible: Boolean = true,
    val tab: MenuTab? = null,
) : UiState