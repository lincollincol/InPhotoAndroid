package com.linc.inphoto.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E>(
    private val router: Router
) : ViewModel() {

    private val _uiState = MutableStateFlow<S?>(null)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableStateFlow<E?>(null)
    val uiEffect = _uiEffect.asStateFlow()

    abstract fun onCoroutineError(e: Exception)

    protected suspend fun setState(state: S?) {
        state?.let {
            _uiState.emit(it)
        }
    }

    protected suspend fun setEffect(effect: E?) {
        effect?.let {
            _uiEffect.emit(it)

            // Flush previous effect
            _uiEffect.emit(null)
        }
    }

    protected fun launchCoroutine(
        task: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                task.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
                onCoroutineError(e)
            }
        }
    }

    open fun onBackPressed() {
        router.exit()
    }

}