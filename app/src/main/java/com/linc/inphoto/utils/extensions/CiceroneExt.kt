package com.linc.inphoto.utils.extensions

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.runBlocking

fun <T> Router.setResultListener(
    key: String,
    onResult: suspend (T?) -> Unit
) {
    setResultListener(key) { result ->
        runBlocking { onResult(result as? T) }
    }
}