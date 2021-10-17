package com.linc.inphoto.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.linc.inphoto.ui.base.viewmodel.BaseViewModel
import com.linc.inphoto.utils.Inflate
import com.linc.inphoto.utils.UnusedResult
import kotlinx.coroutines.flow.collect

abstract class BaseFragment<B : ViewBinding, V : BaseViewModel<S, E>, S, E> : Fragment() {

    protected var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                state?.let { handleUiState(state) }
            }
        }

        lifecycleScope.launchWhenResumed {
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
//        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
//        return _binding?.root
        _binding = getViewBinding()
//        _binding = inflateViewBinding().invoke(inflater, container, false)
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @UnusedResult
    abstract fun handleUiState(state: S) : Any?

    @UnusedResult
    abstract fun handleUiEffect(effect: E) : Any?

    abstract fun getViewBinding() : B

}