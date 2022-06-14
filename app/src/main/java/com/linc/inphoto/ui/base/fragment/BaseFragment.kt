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
import com.linc.inphoto.utils.extensions.isChildFragmentVisible
import com.linc.inphoto.utils.extensions.safeCast
import kotlinx.coroutines.CancellationException
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId), FragmentBackPressedListener, TabStateListener {

    protected abstract val viewModel: BaseViewModel<out UiState>

    protected abstract suspend fun observeUiState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeStartedLaunch {
            observeUiState()
        }
        KeyboardVisibilityEvent.setEventListener(
            requireActivity(),
            viewLifecycleOwner
        ) {
            if (isChildFragmentVisible()) onKeyboardStateChanged(it)
        }
        viewModel.setupContainerId(findContainerId())
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    override fun onTabStateChanged(hidden: Boolean) {
        // Not implemented
    }

    protected open fun onKeyboardStateChanged(hidden: Boolean) {
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

    private fun findContainerId(): String? {
        var fragment = parentFragment
        while (fragment != null) {
            fragment.safeCast<NavContainer>()?.let { return it.containerId }
            fragment = fragment.parentFragment
        }
        return null
    }

}