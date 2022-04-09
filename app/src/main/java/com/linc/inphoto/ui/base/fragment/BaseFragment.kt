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
import kotlinx.coroutines.CancellationException
import timber.log.Timber

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId), FragmentBackPressedListener {

    protected abstract val viewModel: BaseViewModel<out UiState>

    protected abstract suspend fun observeUiState()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeStartedLaunch {
            observeUiState()
        }
        viewModel.setupTabId((parentFragment as? NavContainer)?.containerId)
        Timber.d((parentFragment as? NavContainer)?.containerId)
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
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
}