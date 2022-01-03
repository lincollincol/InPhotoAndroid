package com.linc.inphoto.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.linc.inphoto.ui.base.state.UiState
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.FragmentBackPressedListener
import kotlinx.coroutines.CancellationException

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel<out UiState>> :
    Fragment(), FragmentBackPressedListener {

    protected abstract val viewModel: ViewModel

    protected abstract val binding: Binding

    protected abstract suspend fun observeUiState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeStartedLaunch {
            observeUiState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
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