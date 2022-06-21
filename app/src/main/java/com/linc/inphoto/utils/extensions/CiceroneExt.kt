package com.linc.inphoto.utils.extensions

import com.linc.inphoto.ui.navigation.AppRouter

fun AppRouter.exitWithResult(key: String, data: Any, dialog: Boolean = false) {
    if (dialog) closeDialog() else exit()
    sendResult(key, data)
}