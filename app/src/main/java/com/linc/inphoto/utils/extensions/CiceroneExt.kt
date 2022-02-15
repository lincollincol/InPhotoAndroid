package com.linc.inphoto.utils.extensions

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.runBlocking

@Throws(Exception::class)
fun <T> Router.setResultListener(
    key: String,
    onResult: suspend (Result<T?>) -> Unit,
) {
    setResultListener(key) { result ->
        runBlocking {
            try {
                onResult(Result.success(result as? T))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }
}