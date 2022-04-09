package com.linc.inphoto.ui.main

import com.linc.inphoto.R

enum class MenuTab(val id: Int) {
    HOME(R.id.homeMenuItem),
    FEED(R.id.feedMenuItem),
    CHATS(R.id.chatMenuItem),
    PROFILE(R.id.profileMenuItem);

    companion object {
        @JvmStatic
        fun fromId(id: Int) = values().find { it.id == id }
    }
}