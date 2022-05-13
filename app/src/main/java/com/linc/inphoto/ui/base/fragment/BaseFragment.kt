package com.linc.inphoto.ui.base.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.ui.navigation.FragmentBackPressedListener
import com.linc.inphoto.ui.navigation.NavContainer
import com.linc.inphoto.ui.navigation.TabStateListener
import com.linc.inphoto.utils.extensions.safeCast
import kotlinx.coroutines.CancellationException
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId), FragmentBackPressedListener, TabStateListener {

    protected abstract val viewModel: BaseViewModel<out UiState>

    private var keyboardListener: Unregistrar? = null

    protected abstract suspend fun observeUiState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeStartedLaunch {
            observeUiState()
        }
        viewModel.setupContainerId(findContainerId())
        registerStateKeyboardListener()
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    override fun onTabStateChanged(hidden: Boolean) {
        if (hidden) {
            keyboardListener?.unregister()
            keyboardListener = null
            return
        }
        registerStateKeyboardListener()
    }

    protected open fun onKeyboardStateChanged(visible: Boolean) {
        // Not implemented
    }

    protected open fun showInfoMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
            .show()
    }

    protected open fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun safeResumedLaunch(task: suspend () -> Unit) =
        lifecycleScope.launchWhenResumed {
            safeLaunch(task)
        }

    protected fun safeStartedLaunch(task: suspend () -> Unit) =
        lifecycleScope.launchWhenStarted {
            safeLaunch(task)
        }

    protected fun safeCreatedLaunch(task: suspend () -> Unit) =
        lifecycleScope.launchWhenCreated {
            safeLaunch(task)
        }

    private suspend fun safeLaunch(task: suspend () -> Unit) {
        try {
            task.invoke()
        } catch (e: CancellationException) {
            // Ignored
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorMessage(e.localizedMessage)
        }
    }

    private fun registerStateKeyboardListener() {
        if (keyboardListener != null) {
            return
        }
        keyboardListener = KeyboardVisibilityEvent.registerEventListener(
            requireActivity(),
            ::onKeyboardStateChanged
        )
    }

    private fun findContainerId(): String? {
        var fragment = parentFragment
        while (fragment != null) {
            fragment.safeCast<NavContainer>()?.let { return it.containerId }
            fragment = fragment.parentFragment
        }
        return null
    }

}