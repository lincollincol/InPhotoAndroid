package com.linc.inphoto.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.FragmentBackPressedListener
import com.linc.inphoto.utils.Inflate
import com.linc.inphoto.utils.UnusedResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import java.lang.Exception

abstract class BaseFragment<B : ViewBinding, V : BaseViewModel<S, E>, S, E> : Fragment(), FragmentBackPressedListener {

    protected abstract val viewModel: V

    protected val binding get() = _binding!!
    private var _binding: B? = null

    @UnusedResult
    abstract fun handleUiState(state: S) : Any?

    @UnusedResult
    abstract fun handleUiEffect(effect: E) : Any?

    abstract fun getViewBinding() : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeStartedLaunch {
            viewModel.uiState.collect { state ->
                state?.let { handleUiState(state) }
            }
        }
        safeResumedLaunch {
            viewModel.uiEffect.collect { effect ->
                effect?.let { handleUiEffect(effect) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
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